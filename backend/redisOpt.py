import redis
import json
from lib.pytools.pyBases import *
from lib.pytools.pyLog import Logger

conn = null
log = Logger("redisOpt", enableDebug=true)

def initConnection():
    global conn
    conn = redis.Redis(host="localhost",port=6379,db=2)

def set(key, value, expireTime = -1):
    if not type(value) == str:
        value = json.dumps(value)
    if expireTime == -1:
        conn.set(key, value)
    else:
        conn.set(key, value, ex=expireTime)

def get(key, retJson=false):
    if retJson:
        data = conn.get(key)
        if data == None:
            return data
        data = data.decode()
        log.debug(data)
        return json.loads(data)
    else:
        data = conn.get(key)
        return None if data is None else data.decode()

def delete(key):
    conn.delete(key)

def close():
    conn.close()