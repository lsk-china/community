from lib.pytools.pyBases import *
from lib.pytools.pyLog import Logger
from dao import queryAllMessages, addMessage, deleteMessage, queryMessageById
from dao import queryUserById
from auth import uid
from model import convertDBUsertoUser, convertStringtoPermission
from utils import JsonReturn, HttpException, CheckRequestKey
import json
import time

log = Logger("message", enableDebug=true)
MESSAGES_PER_PAGE = 5
ALLOWED_CREATE_MESSAGE_TYPES = ["NORMAL"]
ALLOWED_SHOW_PERMISSIONS = ["USER","MANAGER","ADMIN"]

class MessageFilter:
    def __init__(self):
        pass
    def match(self, message):
        pass

class IDFilter(MessageFilter):
    def __init__(self, id):
        self.id = id
        MessageFilter.__init__(self)
    def match(self, message):
        return message[1] == self.id

class PermissionFilter(MessageFilter):
    def __init__(self, permission):
        self.permission = permission
        MessageFilter.__init__(self)
    def match(self, message):
        return self.permission.hasPermission(convertStringtoPermission(message[3]))

class TypeFilter(MessageFilter):
    def __init__(self, type):
        self.type = type
        MessageFilter.__init__(self)
    def match(self, message):
        return message[5] == self.type

class PageFilter(MessageFilter):
    def __init__(self, page):
        t = page * MESSAGES_PER_PAGE
        self.biggestID = t
        self.smallestID = t - MESSAGES_PER_PAGE + 1
        MessageFilter.__init__(self)
    def match(self, message):
        return self.smallestID <= message[0] <= self.biggestID

class PlaceholderFilter(MessageFilter):
    def __init__(self):
        MessageFilter.__init__(self)
    def match(self, message):
        messageType = message[5]
        return not messageType == "PLACEHOLDER"

def deleteMessages(deleters, messages):
    nextID = 1
    deleted = []
    for i in range(len(messages)):
        deleterResults = True
        for deleter in deleters:
            deleterResults &= deleter.match(messages[i])
        if deleterResults:
            listMessage = []
            message = messages[i]
            listMessage.append(nextID)
            listMessage.append(message[1])
            listMessage.append(message[2])
            listMessage.append(message[3])
            listMessage.append(message[4])
            listMessage.append(message[5])
            deleted.append(listMessage)
            nextID += 1
    return deleted

def serachMessages(filters, deleters, messages):
    result = []
    deleted = deleteMessages(deleters, messages)
    log.debug(deleted)
    for i in range(len(deleted)):
        filterResult = True
        for filter in filters:
            filterResult &= filter.match(deleted[i])
        if filterResult:
            message = {
                "id": deleted[i][0],
                "creater": deleted[i][1],
                "createTime": deleted[i][2],
                "content": deleted[i][4],
                "type": deleted[i][5]
            }
            result.append(message)
    return result

@JsonReturn
def applyJoin(token, qq):
    id = uid(token)
    user = convertDBUsertoUser(queryUserById(id)[0])
    content = {
        "username": user.getName(),
        "qq": qq
    }
    addMessage(id, time.asctime(time.localtime()), 'MANAGER', json.dumps(content), "APPLY_JOIN")
    return "Success"

@JsonReturn
def isApplyJoinSent(token):
    id = uid(token)
    messages = queryAllMessages()
    filtered = serachMessages([IDFilter(id)], [], messages)
    return not len(filtered) == 0
"""
    计算分页的总页数
    1.删除全部PLACEHOLDER消息。PLACEHOLDER消息为了防止数据库自增出现问题，不需要参与显示和处理。
    2.如果最后一条消息的ID能被每页消息总数整除的话，直接返回商；如果不能整除，就返回lastMessageID // MESSAGES_PER_PAGE + 1， 加1是因为要加一页显示ID为lastMessageID / MESSAGES_PER_PAGE 余数的那些消息
"""
def calcTotalPages(dbResult, deleters):
    deleted = deleteMessages(deleters, dbResult)
    lastMessageID = dbResult[len(deleted) - 1][0]
    totalPages = lastMessageID / MESSAGES_PER_PAGE if (lastMessageID % MESSAGES_PER_PAGE) == 0 else (lastMessageID // MESSAGES_PER_PAGE) + 1
    return totalPages

@JsonReturn
def messages(token, filter, page):
    dbResult = queryAllMessages()
    id = uid(token)
    user = convertDBUsertoUser(queryUserById(id)[0])
    filters = [PageFilter(page)]
    deleters = [PlaceholderFilter(), PermissionFilter(user.getPermission())]
    if filter == 0:             # 0: All Message
        pass
    elif filter == 1:           # 1: Only APPLY_JOIN
        filters.append(TypeFilter("APPLY_JOIN"))
    elif filter == 2:           # 2: Only WORK_CHECK
        filters.append(TypeFilter("WORK_CHECK"))
    else:
        raise HttpException(400, "Unknown filter type")
    messages = serachMessages(filters, deleters, dbResult)
    log.debug(messages)
    totalPages = calcTotalPages(dbResult, deleters)
    return {
        "totalPages": totalPages,
        "result": messages
    }

@JsonReturn
def delete(token, mid):
    id = uid(token)
    user = convertDBUsertoUser(queryUserById(id)[0])
    dbResult = queryMessageById(mid)[0]
    if not user.getPermission().hasPermission(convertStringtoPermission(dbResult[3])):
        raise HttpException(403, "")
    deleteMessage(mid)
    return "Success"

@JsonReturn
@CheckRequestKey("createMessage")
def createMessage(token, content, type, showPermission, requestKey):
    if not ALLOWED_CREATE_MESSAGE_TYPES.__contains__(type):
        raise HttpException(403, "Message type is not allowed")
    if not ALLOWED_SHOW_PERMISSIONS.__contains__(showPermission):
        raise HttpException(403, "Show permission is not allowed")
    creater = uid(token)
    createDate = time.asctime(time.localtime())
    addMessage(creater, createDate, showPermission, content, type)
    return "Success."
