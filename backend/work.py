from dao import addWork, queryAllWorks, enableWorkById, queryWorkById, increaseWorkAssets, queryWorksByOwner, updateWorkName, increaseWorkViews
from dao import queryAssets, queryAssetById, increaseAssetDownloadsById
from dao import addAsset as dbAddAsset
from auth import uid
from utils import JsonReturn, CheckRequestKey, CheckPermission, HttpException, now
from model import MANAGER
from redisOpt import get, set, delete
from lib.pytools.pyLog import Logger

from flask import send_file

import os
import time

MAX_WORKS_PER_PAGE = 10
log = Logger("work", enableDebug=True)

def pagination(src, page):
    changedID = []
    nextID = 1
    for workInstance in src:
        changedID.append({
            "id": nextID,
            "owner": workInstance["owner"],
            "name": workInstance["name"],
            "createDate": workInstance["createDate"],
            "enable": workInstance["enable"],
            "assets": workInstance["assets"],
            "realID": workInstance["wid"],
            "description": workInstance["description"]
        })
        nextID += 1
    log.debug(changedID)
    maxID = page * MAX_WORKS_PER_PAGE
    # 当前页的最小ID即上一页的最大ID+1
    minID = (page - 1) * MAX_WORKS_PER_PAGE + 1
    result = []
    for workInstance in changedID:
        if minID <= workInstance["id"] <= maxID:
            result.append(workInstance)
    return result

@JsonReturn
def queryAllWork(page):
    dbResult = queryAllWorks()
    noDisable = []
    for i in range(len(dbResult)):
        worc = {}
        worc["wid"] = dbResult[i][0]
        worc["owner"] = dbResult[i][1]
        worc["name"] = dbResult[i][2]
        worc["createDate"] = dbResult[i][3]
        worc['enable'] = dbResult[i][4]
        worc['assets'] = dbResult[i][5]
        worc['views'] = dbResult[i][6]
        worc['description'] = dbResult[i][7]
        noDisable.append(worc)
    return pagination(noDisable, page)

@JsonReturn
@CheckRequestKey("createWork")
def createWork(token, requestKey, name):
    id = uid(token)
    createTime = time.asctime(time.localtime())
    workID = addWork(id, name, createTime)
    os.mkdir("../fileserver/server/work/"+str(workID))
    os.mkdir("../fileserver/server/work/"+str(workID)+"/assets")
    set(str(workID) + "-ASSETS", 0)
    return "Success"

@JsonReturn
@CheckPermission(MANAGER)
def enableWork(token, wid):
    enableWorkById(wid)
    return "Success"

@JsonReturn
def addAsset(request):
    # token = request.cookies.get('token')
    # wid = request.values["wid"]
    # workAssets = int(get(str(wid) + "-ASSETS"))
    # if workAssets is None:
    #     workAssets = workInstance[6]
    # workAssets += 1
    # set(str(wid) + "-ASSETS", workAssets)
    # hasNext = request.values['hasNext']
    # if hasNext is "no":
    #     changeWorkAssets(workAssets, wid)
    #     delete(str(wid) + "-ASSETS")
    # return "Success"
    token = request.cookies.get("token")
    wid = int(request.values["wid"])
    name = request.files['file'].filename
    workInstance = queryWorkById(wid)
    currentUserID = uid(token)
    if not workInstance[1] == currentUserID:
        raise HttpException(403, "Permission dined")
    aid = dbAddAsset(name, wid, "WORK", now())
    increaseWorkAssets(wid)
    file = request.files['file']
    file.save("../fileserver/server/work/" + str(wid) + "/assets/" + str(aid) + ".zip")
    return "Success"

@JsonReturn
def queryWork(wid):
    dbResult = queryWorkById(int(wid))
    result = {
        "wid": dbResult[0],
        "ownerID": dbResult[1],
        "name": dbResult[2],
        "createDate": dbResult[3],
        "enable": dbResult[4],
        "assets": dbResult[5],
        "views": dbResult[6],
        "description": dbResult[7]
    }
    return result

@JsonReturn
def queryWorksByUser(token, page):
    id = uid(token)
    dbResult = queryWorksByOwner(id)
    result = []
    for i in range(len(dbResult)):
        result.append({
            "wid": dbResult[i][0],
            "owner": dbResult[i][1],
            "name": dbResult[i][2],
            "createDate": dbResult[i][3],
            "enable": dbResult[i][4],
            "assets": dbResult[i][5],
            "views": dbResult[i][6],
            "description": dbResult[i][7]
        })
    return pagination(result, page)

@JsonReturn
def uploadWorkHDImg(request):
    token = request.cookies.get("token")
    id = uid(token)
    wid = int(request.values["wid"])
    workInstance = queryWorkById(wid)
    if not workInstance[1] == id:
        raise HttpException(403, "Permission dined")
    file = request.files["file"]
    file.save("../fileserver/server/work/"+str(wid)+"/hdImg.jpg")
    return "Success"

@JsonReturn
def changeWorkName(token, wid, newName):
    userID = uid(token)
    workInstance = queryWorkById(wid)
    if not userID == workInstance[1]:
        raise HttpException(403, "Permission dined")
    updateWorkName(newName, wid)
    return "Success"

@JsonReturn
def queryWorkAssets(wid):
    dbResult = queryAssets(wid, "WORK")
    result = []
    for i in range(len(dbResult)):
        result.append({
            "aid": dbResult[i][0],
            "name": dbResult[i][1],
            "srcID": dbResult[i][2],
            "srcType": dbResult[i][3],
            "uploadTime": dbResult[i][4],
            "downloads": dbResult[i][5]
        })
    return result

def downloadAsset(aid):
    asset = queryAssetById(aid)
    name = asset[1]
    if asset is None:
        raise HttpException(404, "asset doesn't exists")
    wid = asset[2]
    assetLocation = '../fileserver/server/work/' + str(wid) + '/assets/' + str(aid) + ".zip"
    increaseAssetDownloadsById(aid)
    return send_file(assetLocation, as_attachment=True, attachment_filename=name)

@JsonReturn
def incViews(wid):
    increaseWorkViews(wid)
    return "Success"
