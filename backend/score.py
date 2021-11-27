from utils import JsonReturn, HttpException
from dao import updateScore, queryUserById, queryClockInStatistic, updateClockInStatistic
from auth import uid, checkRequestKey
from redisOpt import get, set
from lib.pytools.pyBases import *
from lib.pytools.pyLog import Logger
from model import convertJsonToUser, convertUsertoJson, convertDBUsertoUser

import time

log = Logger("score", enableDebug=true)

@JsonReturn
def updateUserScore(score, id):
    updateScore(int(score), id)
    return "Success"

def rawCanClockIn(id):
    log.debug(str(id) + '-LAST-CLOCK-IN-TIME')
    lastClockInTime = int(get(str(id) + '-LAST-CLOCK-IN-TIME'))
    log.debug(lastClockInTime)
    return (round(time.time()) - lastClockInTime) >= 86400


@JsonReturn
def clockin(token, requestKey, id):
    if not checkRequestKey(token, 'clockin', requestKey):
        raise HttpException(403, "RequestKey doesn't match")
    if not rawCanClockIn(id):
        raise HttpException(403, "Already clock in")
    user = convertDBUsertoUser(queryUserById(id)[0])
    score = user.getScore()
    score += 10
    updateUserScore(score, id)
    log.debug(user.getID())
    set(str(user.getID()) + "-LAST-CLOCK-IN-TIME", round(time.time()))
    statistic = rawClockInStatistic(token)
    totalTimes = int(statistic["totalTimes"]) + 1
    totalScore = int(statistic["totalScore"]) + 10
    log.debug(totalTimes)
    log.debug(totalScore)
    log.debug(statistic)
    updateClockInStatistic(id, totalTimes, totalScore)
    return "Success"

def rawClockInStatistic(token):
    id = uid(token)
    dbResult = queryClockInStatistic(id)
    log.debug(dbResult)
    lastClockInTime = int(get(str(id) + '-LAST-CLOCK-IN-TIME'))
    result = {
        "totalTimes": dbResult[0][1],
        "totalScore": dbResult[0][2],
        "lastClockInTime": time.asctime(time.localtime(lastClockInTime))
    }
    return result

@JsonReturn
def clockInStatistic(token):
    return rawClockInStatistic(token)

@JsonReturn
def canClockIn(token):
    id = uid(token)
    log.debug(id)
    return rawCanClockIn(id)
