from lib.advpymysql.core.annotations import *


@Select("select * from user")
def queryAllUsers():
    pass


@Select("select * from user where id=#{id}")
def queryUserById(id):
    pass


@Select("select * from user where name=#{name}")
def queryUserByName(name):
    pass

@Insert("insert into user(name,password,role,score,mail) values(#{name},#{password},#{permission},#{score},#{mail})")
def addUser(name, password, permission, score, mail):
    pass

@Update("update user set score=#{score} where id=#{id}")
def updateScore(score, id):
    pass

@Select("select * from clockInStatistic where id=#{id}")
def queryClockInStatistic(id):
    pass

@Insert("insert into clockInStatistic(id, totalTimes, totalScore) values(#{id}, #{totalTimes}, #{totalScore})")
def addNewClockInStatisticRecord(id, totalTimes, totalScore):
    pass

@Update("update clockInStatistic set totalTimes=#{totalTimes},totalScore=#{totalScore} where id=#{id}")
def updateClockInStatistic(id, totalTimes, totalScore):
    pass

@Select("select * from message")
def queryAllMessages():
    pass

@Select("select * from message where id=#{id}")
def queryMessageById(id):
    pass

@Insert("insert into message(creater, createDate, showPermission, content, type) values(#{creater}, #{createDate}, #{showPermission}, #{content}, #{type})")
def addMessage(creater, createDate, showPermission, content, type):
    pass

@Delete("update message set type='PLACEHOLDER' where id=#{mid}")
def deleteMessage(mid):
    pass

@Update("update user set role='USER' where name=#{name}")
def userJoin(name):
    pass

@LockTableWrite("venusstudio.works")
@Insert("insert into works(owner,name,createDate) values(#{owner},#{name},#{createDate});", true)
def addWork(owner, name, createDate):
    pass

@Select("select * from works where enable=1")
def queryAllWorks():
    pass

@Update("update works set enable=1 where id=#{id}")
def enableWorkById(id):
    pass

@Select("select * from works where id=#{id}", returnFirst=true)
def queryWorkById(id):
    pass

@Update("update works set assets=assets+1 where id=#{id}")
def increaseWorkAssets(id):
    pass

@Select("select * from works where owner=#{owner}")
def queryWorksByOwner(owner):
    pass

@Update("update works set name=#{newName} where id=#{id}")
def updateWorkName(newName, id):
    pass

@Insert("insert into replies(srcType, srcID, senderID, receiverID, content, sendDate) values(#{srcType}, #{srcID}, #{senderID}, #{receiverID}, #{content}, #{sendDate})", readID=true)
def addNewReply(srcType, srcID, senderID, receiverID, content, sendDate):
    pass

@Select("select * from replies where srcType=#{srcType} and srcID=#{srcID}")
def queryRepliesBySrc(srcType, srcID):
    pass

@Select("select * from replies where receiverID=#{receiverID}")
def queryRepliesByReceiver(receiverID):
    pass

@Delete("delete from replies where id={rid}")
def deleteReply(rid):
    pass

@Select("select * from assets where srcID=#{srcID} and srcType=#{srcType}")
def queryAssets(srcID, srcType):
    pass

@Insert("insert into assets(name, srcID, srcType, uploadTime) values(#{name}, #{srcID}, #{srcType}, #{uploadTime})")
def addAsset(srcID, srcType, name, uploadTime):
    pass

@Update("update assets set name=#{name} where id=#{id}")
def renameAssetById(name, id):
    pass

@Delete("delete from assets where id=#{id}")
def deleteAssetById(id):
    pass

@Insert("insert into likes(targetID, targetType, operatorID) values(#{targetID}, #{targetType}, #{operatorID})")
def likeSomething(targetID, targetType, operatorID):
    pass

@Select("select count(id) from likes where targetId=#{targetID} and targetType=#{targetType}", returnFirst=true)
def likes(targetID, targetType):
    pass

@Select("select count(id) from likes where targetId=#{targetID} and targetType=#{targetType} and operatorID=#{operatorID}", returnFirst=true)
def isLiked(targetID, targetType, operatorID):
    pass

@Update("update assets set downloads=downloads+1 where id=#{id}")
def increaseAssetDownloadsById(id):
    pass

@Select("select * from assets where id=#{id}", returnFirst=true)
def queryAssetById(id):
    pass

@Insert("insert into assets(name,srcID,srcType,uploadTime) values(#{name}, #{srcID}, #{srcType}, #{uploadTime})", readID=true)
def addAsset(name, srcID, srcType, uploadTime):
    pass

@Delete("delete from likes where targetID=#{targetID} and targetType=#{targetType} and operatorID=#{operatorID}")
def deLikeSomething(targetID, targetType, operatorID):
    pass

@Update("update works set views=views+1 where id=#{id}")
def increaseWorkViews(id):
    pass

@Select("select count(id) from replies where srcType=#{srcType} and srcID=#{srcID}", returnFirst=true)
def queryReplyCount(srcType, srcID):
    pass
