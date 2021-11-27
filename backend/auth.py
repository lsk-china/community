import time
import json
import uuid
import random
import hashlib
from redisOpt import *
from utils import JsonReturn, HttpException, defaultHead, CheckRequestKey
from dao import queryUserByName, addUser, queryUserById, addNewClockInStatisticRecord, userJoin
from dao import addMessage, queryAllMessages, queryMessageById, deleteMessage
from model import convertDBUsertoUser, User, convertUsertoJson
from model import ADMIN, MANAGER, USER, GUEST
from lib.pytools.pyLog import Logger
from lib.pytools.pyMail import send
from lib.pytools.pyBases import replaceAll
from PIL import Image, ImageDraw, ImageFont

log = Logger("auth", enableDebug=true)
sha256 = hashlib.sha256()

def newToken():
    token = str(uuid.uuid4())
    set(token+"-STATE","GUEST")
    return token

@JsonReturn
def login(token, username, password):
    if token == null:
        raise HttpException(400, "Please get a token first.")
    dbUser = queryUserByName(username)
    log.debug(dbUser)
    if len(dbUser) == 0:
        raise HttpException(405, "User not found")
    user = convertDBUsertoUser(dbUser[0])
    if not user.getPassword() == password:
        raise HttpException(500, "Incorrect password")
    id = user.getID()
    set(token + "-STATE","LOGIN")
    set(token + "-UID", str(id))
    set(token + "-USER-INFO", convertUsertoJson(user))
    set(token + "-PERMISSION", str(user.getPermission()))
    return "SUCCESS"

@JsonReturn
@CheckRequestKey("register")
def register(token, username, password, mail, requestKey):
    user = User()
    user.setID(0)
    user.setName(username)
    user.setMail(mail)
    user.setPassword(password)
    user.setPermission(GUEST)
    user.setScore(0)
    set(token+"-REG-USERINFO", convertUsertoJson(user))
    sha256.update(str(uuid.uuid4()).encode())
    regCode = str(sha256.hexdigest())
    set(token + "-REG-CODE", regCode)
    mailText = "Your register code is " + regCode
    send(mailText, "3075929352@qq.com", mail, "Register")
    return "Successfully sent registration mail."

@JsonReturn
def doneRegister(token, regCode):
    redisRegCode = get(token + "-REG-CODE")
    log.debug(redisRegCode)
    redisRegCode = replaceAll(redisRegCode, "-", "")
    if not redisRegCode == regCode:
        raise HttpException(500, "RegCode doesn't match")
    regUserInfo = get(token + "-REG-USERINFO", retJson=true)
    addUser(regUserInfo["name"], regUserInfo["password"], regUserInfo["permission"], regUserInfo["score"], regUserInfo["mail"])
    login(token, regUserInfo["name"], regUserInfo["password"])
    id = uid(token)
    defaultHead(id)
    delete(token+"-REG-USERINFO")
    delete(token + "-REG-CODE")
    set(str(id) + "-LAST-CLOCK-IN-TIME", 0)
    addNewClockInStatisticRecord(id, 0, 0)
    return "Success"

@JsonReturn
def userinfo(token):
    state = get(token + "-STATE")
    if state == "GUEST":
        raise HttpException(403, "Not logined")
    uid = str(get(token + "-UID"))
    user = convertDBUsertoUser(queryUserById(uid)[0])
    return user

def uid(token):
    return int(get(token + "-UID"))

def username(id):
    result = get(str(id) + "-USERNAME")
    if result is None:
        user = convertDBUsertoUser(queryUserById(uid)[0])
        set(str(id) + "-USERNAME", user.name)
        return user.name
    else:
        return result

@JsonReturn
def logout(token):
    delete(token + "-UID")
    delete(token + "-PERMISSION")
    set(token + "-STATE", "GUEST")
    return "Success"

def get_random_color():
    return (random.randint(0, 255), random.randint(0, 255), random.randint(0, 255))

@JsonReturn
def generateVaildCode():
    codeImage = Image.new("RGB", (300, 34), (255, 255, 255))
    draw = ImageDraw.Draw(codeImage)
    font = ImageFont.truetype("consola.ttf",20)
    codeStr = ""
    for i in range(0, 6):
        random_num = str(random.randint(0, 9))
        random_low_alpha = chr(random.randint(97, 122))
        random_high_alpha = chr(random.randint(65, 90))
        random_char = random.choice([random_num, random_low_alpha, random_high_alpha])
        draw.text((i * 50 + 20, 5), random_char, get_random_color(), font=font)
        codeStr += random_char
    width = 260
    height = 34
    for i in range(6):
        x1 = random.randint(0, width)
        x2 = random.randint(0, width)
        y1 = random.randint(0, height)
        y2 = random.randint(0, height)
        draw.line((x1, y1, x2, y2), fill=get_random_color())

    for i in range(6):
        draw.point([random.randint(0, width), random.randint(0, height)], fill=get_random_color())
        x = random.randint(0, width)
        y = random.randint(0, height)
        draw.arc((x, y, x + 4, y + 4), 0, 90, fill=get_random_color())
    codeID = str(uuid.uuid4())
    codeID = replaceAll(codeID, "-", "")
    set(codeID + '-VALUE', codeStr)
    codeImage.save("/tmp/community/"+codeID+".jpg")
    codeImage.close()
    return codeID

@JsonReturn
def checkValidCode(token, codeID, codeStr, forUrl):
    redisCodeStr = get(codeID + "-VALUE")
    if not redisCodeStr == codeStr:
        delete(codeID + "-VALUE")
        return "Not match"
    requestKey = str(uuid.uuid4())
    set(token + "-REQUESTKEY-"+forUrl, requestKey, expireTime=86400)
    delete(codeID + "-VALUE")
    os.remove("/tmp/community/"+codeID+".jpg")
    return requestKey

def checkRequestKey(token, api, requestKey):
    redisRequestKey = get(token + "-REQUESTKEY-"+api)
    delete(token + "-REQUESTKEY-"+api)
    return requestKey == redisRequestKey

@JsonReturn
def allowJoin(token, messageID):
    id = uid(token)
    processUser = convertDBUsertoUser(queryUserById(id)[0])
    if not processUser.getPermission().hasPermission(MANAGER):
        raise HttpException(403, "Permission dined")
    message = queryMessageById(messageID)
    content = message[0][4]
    username = json.loads(content)["username"]
    userJoin(username)
    deleteMessage(messageID)
    return "Success"

@JsonReturn
def findUserById(uid):
    dbResult = queryUserById(uid)[0]
    return convertUsertoJson(convertDBUsertoUser(dbResult))
