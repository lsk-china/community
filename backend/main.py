import sys

sys.path.append("lib")
from lib.advpymysql.core.tools import setupConnection
from lib.pytools.pyLog import *
from lib.pytools.pyBases import *
from lib.pytools.pyMail import connect
from flask import Flask, request, make_response, send_from_directory
from redisOpt import initConnection,close
from flask_cors import CORS
import shutil
import auth
import score
import message
import work
import reply
import like

log = Logger("main", enableDebug=true)
app = Flask(__name__)
CORS(app, supports_credentials=True)

@app.route("/token")
def token():
    resp = make_response()
    resp.set_cookie("token",auth.newToken())
    return resp

@app.route("/login",methods=["POST"])
def login():
    username = request.values['username']
    password = request.values['password']
    token = request.cookies.get("token")
    return auth.login(token, username, password)

@app.route("/register",methods=["POST"])
def register():
    username = request.values['username']
    password = request.values['password']
    email = request.values['email']
    token = request.cookies.get("token")
    requestKey = request.values['requestKey']
    return auth.register(token, username, password, email, requestKey)

@app.route("/doneRegister", methods=["GET"])
def doneRegister():
    regCode = request.values["regCode"]
    token = request.cookies.get("token")
    log.debug(regCode)
    return auth.doneRegister(token, regCode)

@app.route("/userinfo", methods=["GET"])
def userinfo():
    token = request.cookies.get("token")
    return auth.userinfo(token)

@app.route("/updateScore")
def updateScore():
    token = request.cookies.get("token")
    s = request.values["score"]
    return score.update(token, s)

@app.route("/uploadHead", methods=["POST"])
def uploadHead():
    f = request.files['file']
    token = request.cookies.get('token')
    p = "../fileserver/server/head/" + str(auth.uid(token)) + ".jpg"
    f.save(p)
    return "Success"

@app.route("/clockin")
def clockin():
    token = request.cookies.get('token')
    requestKey = request.values['requestKey']
    id = auth.uid(token)
    return score.clockin(token, requestKey, id)

@app.route("/canClockIn")
def canClockIn():
    token = request.cookies.get("token")
    return score.canClockIn(token)

@app.route("/logout")
def logout():
    token = request.cookies.get("token")
    return auth.logout(token)

@app.route("/generateValidCode")
def generateValidCode():
    return auth.generateVaildCode()

@app.route("/getValidCode")
def getValidCode():
    codeID = request.values["codeid"]
    return send_from_directory("/tmp/community/", codeID + ".jpg", as_attachment=True)

@app.route("/checkValidCode")
def checkValidCode():
    codeID = request.values["codeID"]
    codeStr = request.values["codeStr"]
    forUrl = request.values["forAPI"]
    token = request.cookies.get("token")
    return auth.checkValidCode(token, codeID, codeStr, forUrl)

@app.route("/clockInStatistic")
def clockInStatistic():
    token = request.cookies.get("token")
    return score.clockInStatistic(token)

@app.route("/applyJoin")
def applyJoin():
    token = request.cookies.get("token")
    qq = request.values['qq']
    return message.applyJoin(token, qq)

@app.route("/isApplySent")
def isApplySent():
    token = request.cookies.get("token")
    return message.isApplyJoinSent(token)

@app.route("/allowJoin")
def allowJoin():
    messageID = request.values["messageID"]
    token = request.cookies.get("token")
    return auth.allowJoin(token, messageID)

@app.route("/messages")
def messages():
    token = request.cookies.get("token")
    filter = int(request.values['filter'])
    page = int(request.values['page'])
    return message.messages(token, filter, page)

@app.route("/deleteMessage")
def deleteMessage():
    token = request.cookies.get("token")
    mid = int(request.values['mid'])
    return message.delete(token, mid)

@app.route("/createMessage")
def createMessage():
    token = request.cookies.get("token")
    requestKey = request.values["requestKey"]
    type = request.values["type"]
    showPermission = request.values["showPermission"]
    content = request.values["content"]
    return message.createMessage(token, content, type, showPermission, requestKey)

@app.route("/queryAllWorks")
def queryAllWorks():
    return work.queryAllWork(int(request.values["page"]))

@app.route("/addWork")
def createWork():
    token = request.cookies.get("token")
    requestKey = request.values["requestKey"]
    name = request.values["name"]
    return work.createWork(token, requestKey, name)

@app.route("/enableWork")
def enableWork():
    token = request.cookies.get("token")
    wid = int(request.values["wid"])
    return work.enableWork(token, wid)

@app.route("/uploadWorkIntroduction")
def uploadWorkIntroduction():
    f = request.files["file"]
    wid = request.values["wid"]
    f.save("../fileserver/server/work/"+str(wid)+"/introduction.md")

@app.route("/uploadWorkAsset", methods=["POST"])
def uploadWorkAsset():
    log.debug(request)
    log.debug(len(request.files))
    log.debug(len(request.values))
    return work.addAsset(request)

@app.route("/queryWorkById")
def queryWorkById():
    wid = int(request.values["wid"])
    return work.queryWork(wid)

@app.route("/queryWorkByUser")
def queryWorkByUser():
    token = request.cookies.get("token")
    page = int(request.values["page"])
    return work.queryWorksByUser(token, page)

@app.route("/uploadWorkHDImg")
def uploadWorkHDImg():
    return work.uploadWorkHDImg(request)

@app.route("/changeWorkName")
def changeWorkName():
    token = request.cookies.get("token")
    wid = int(request.values["wid"])
    newName = request.values["newname"]
    return work.changeWorkName(token, wid, newName)

@app.route("/queryUserById")
def queryUserById():
    id = int(request.values['uid'])
    return auth.findUserById(id)

@app.route("/addReply")
def addReply():
    token = request.cookies.get("token")
    srcType = request.values['srcType']
    srcID = request.values['srcID']
    content = request.values['content']
    return reply.addReply(token, srcType, srcID, content)

@app.route('/querySelfReplies')
def querySelfReplies():
    token = request.cookies.get("token")
    return reply.querySelfReplies(token)

@app.route('/queryReplies')
def queryReplies():
    srcType = request.values['srcType']
    srcID = request.values['srcID']
    return reply.queryReplies(srcType, srcID)

@app.route("/queryWorkAssets")
def queryWorkAssets():
    wid = int(request.values["wid"])
    return work.queryWorkAssets(wid)

@app.route("/downloadWorkAsset")
def downloadWorkAsset():
    aid = int(request.values["aid"])
    return work.downloadAsset(aid)

@app.route('/queryWorkLikes')
def queryWorkLikes():
    wid = int(request.values["wid"])
    return like.queryWorkLikes(wid)

@app.route('/queryReplyLikes')
def queryReplyLikes():
    rid = int(request.values["rid"])
    return like.queryReplyLikes(rid)

@app.route('/isLiked')
def isLiked():
    token = request.cookies.get("token")
    targetType = request.values["targetType"]
    targetID = int(request.values["targetID"])
    return like.selfLiked(token, targetType, targetID)

@app.route("/likeSomething")
def likeSomething():
    token = request.cookies.get("token")
    targetType = request.values["targetType"]
    targetID = int(request.values["targetID"])
    return like.like(token, targetType, targetID)

@app.route("/deLikeSomething")
def deLikeSomething():
    token = request.cookies.get("token")
    targetType = request.values["targetType"]
    targetID = int(request.values["targetID"])
    return like.deLike(token, targetType, targetID)

@app.route("/increaseWorkViews")
def increaseWorkViews():
    wid = int(request.values["wid"])
    work.increaseWorkViews(wid)
    return "Success"

@app.route("/queryWorkReplyCount")
def queryWorkReplyCount():
    wid = int(request.values["wid"])
    return reply.queryWorkReplyCount(wid)

if __name__ == "__main__":
    initConnection()
    connect("smtp.qq.com", "3075929352@qq.com", "euzczusgzholdhee")
    setupConnection("connection.properties")
    app.run(host="0.0.0.0", port=40000)
    close()
