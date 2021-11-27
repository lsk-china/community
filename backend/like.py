from dao import likes, isLiked, likeSomething, deLikeSomething
from utils import HttpException, JsonReturn
from auth import uid

@JsonReturn
def queryWorkLikes(wid):
    return likes(wid, "WORK")[0]

@JsonReturn
def queryReplyLikes(rid):
    return likes(rid, "REPLY")[0]

def selfLiked(token, targetType, targetID):
    operatorID = uid(token)
    dbResult = isLiked(targetID, targetType, operatorID)[0]
    return "true" if dbResult == 1 else "false"

@JsonReturn
def like(token, targetType, targetID):
    operatorID = uid(token)
    if isLiked(targetID, targetType, operatorID)[0] == 1:
        raise HttpException(500, "Liked.")
    likeSomething(targetID, targetType, operatorID)

@JsonReturn
def deLike(token ,targetType, targetID):
    operatorID = uid(token)
    if isLiked(targetID, targetType, operatorID)[0] == 0:
        raise HttpException(500, "Not liked")
    deLikeSomething(targetID, targetType, operatorID)