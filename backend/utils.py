import json

from functools import wraps
from lib.pytools.pyBases import *
from lib.pytools.pyLog import Logger
from lib.advpymysql.core.tools import getParamID
from model import User, convertUsertoJson, convertStringtoPermission
from shutil import copyfile
import redisOpt
import auth
import time

jsonReturnLogger = Logger("JsonReturn")
log = Logger("utils", enableDebug=true)

class HttpException(Exception):
    def __init__(self, code, reason):
        self.code = code
        self.reason = reason
        Exception.__init__(self)

    def getCode(self):
        return self.code

    def getReason(self):
        return self.reason

def JsonReturn(func):
    @wraps(func)
    def doDecorator(*args, **kwargs):
        result = null
        handled = false
        try:
            result = {
                "code": 200,
                "data": func(*args, **kwargs),
                "message": "Success"
            }
        except HttpException as e:
            jsonReturnLogger.error("Error: ", e)
            result = {
                "code": e.getCode(),
                "data": "",
                "message": e.getReason()
            }
            handled = true
        except Exception as e:
            jsonReturnLogger.error("Error: ", e)
            if not handled:
                result = {
                    "code": 500,
                    "data": "",
                    "message": str(e)
                }
            raise e
        obj = null
        if type(result) == User:
            obj = convertUsertoJson(result)
        else:
            obj = result
        return json.dumps(obj)
    return doDecorator

def defaultHead(uid):
    copyfile("../fileserver/server/head/default.jpg", "../fileserver/server/head/" + str(uid) + ".jpg")

def CheckRequestKey(api):
    def doDecorator(func):
        @wraps(func)
        def run(*args, **kwargs):
            requestKeyID = getParamID(func, "requestKey")
            tokenID = getParamID(func, "token")
            requestKey = args[requestKeyID]
            token = args[tokenID]
            if not auth.checkRequestKey(token, api, requestKey):
                raise HttpException(403, "Requestkey doesn't match")
            log.debug(args)
            return func(*args, **kwargs)
        return run
    return doDecorator

def CheckPermission(requiredPermission):
    def doDecorator(func):
        @wraps(func)
        def run(*args, **kwargs):
            tokenID = getParamID(func, "token")
            token = args[tokenID]
            permission = convertStringtoPermission(redisOpt.get(token + "-PERMISSION"))
            if not permission.hasPermission(requiredPermission):
                raise HttpException(403, "Permission Dined")
            return func(*args, **kwargs)
        return run
    return doDecorator

def now():
    return time.asctime(time.localtime())
