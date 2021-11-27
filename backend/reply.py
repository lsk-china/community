from utils import JsonReturn, HttpException, now
from dao import addNewReply, queryRepliesByReceiver, queryRepliesBySrc, deleteReply, queryReplyCount
from dao import queryWorkById
from auth import uid, username


@JsonReturn
def addReply(token, srcType, srcID, content):
    senderID = uid(token)
    if srcType == "work":
        work = queryWorkById(srcID)
        receiverID = work[1]
        dateTime = now()
        rid = addNewReply("WORK", srcID, senderID, receiverID, content, dateTime)
        return {
            "rid": rid,
            "srcType": "WORK",
            "srcID": srcID,
            "senderID": senderID,
            "receiverID": receiverID,
            "content": content,
            "sendDate": dateTime,
            "senderName": username(senderID)
        }
    else:
        raise HttpException(500, "Unknown srcType")

@JsonReturn
def querySelfReplies(token):
    dbResult = queryRepliesByReceiver(uid(token))
    result = []
    for i in range(len(dbResult)):
        result.append({
            "rid": dbResult[i][0],
            "srcType": dbResult[i][1],
            "srcID": dbResult[i][2],
            "senderID": dbResult[i][3],
            "content": dbResult[i][4],
            "receiverID": dbResult[i][5],
            "sendDate": dbResult[i][6]
        })
    return result

@JsonReturn
def queryReplies(srcType, srcID):
    dbResult = queryRepliesBySrc(srcType, srcID)
    result = []
    for i in range(len(dbResult)):
        result.append({
            "rid": dbResult[i][0],
            "srcType": dbResult[i][1],
            "srcID": dbResult[i][2],
            "senderID": dbResult[i][3],
            "content": dbResult[i][4],
            "receiverID": dbResult[i][5],
            "sendDate": dbResult[i][6],
            "senderName": username(dbResult[i][3])
        })
    return result

@JsonReturn
def queryWorkReplyCount(wid):
    return queryReplyCount("WORK", wid)[0]
