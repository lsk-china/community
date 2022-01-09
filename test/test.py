import redis
import requests
from pygments import highlight, lexers, formatters
import json
import hashlib

sess = None
md5 = hashlib.md5()
redisConnPool = redis.ConnectionPool(host="localhost", port=6379, db=2, password="lsk123456", max_connections=100)

def createSession():
    global sess
    sess = requests.Session()

def printJson(jsonIn):
    formattedJson = json.dumps(jsonIn, indent=4, ensure_ascii=False, sort_keys=True)
    colorfulJson = highlight(formattedJson, lexers.JsonLexer(), formatters.TerminalFormatter())
    print(colorfulJson)

def token():
    global sess
    resp = sess.get("http://localhost:11001/token")
    print(resp.cookies)
    printJson(resp.json())

def solveACaptcha(forApi):
    global sess
    resp = sess.get("http://localhost:11003/generateCaptcha")
    codeID = resp.json()["data"]["codeID"]
    print("CodeID: " + codeID)
    redisConn = redis.Redis(connection_pool=redisConnPool)
    codeText = redisConn.get(codeID + "-CODETEXT").decode("utf-8")
    codeText = codeText.replace("\"", "", codeText.count("\""))
    print("CodeText: " + codeText)
    resp2 = sess.get("http://localhost:11003/checkCaptcha", params={"codeID": codeID, "codeText": codeText, "targetURL": forApi})
    printJson(resp2.json())

def loginAsAdmin():
    login("admin", "123456")

def login(identity, password):
    global sess
    solveACaptcha("/login")
    md5.update(password.encode(encoding="utf-8"))
    print(sess.cookies)
    resp = sess.post("http://localhost:11001/login", params={"identity": identity, "password": md5.hexdigest()})
    printJson(resp.json())

def post(url, param):
    resp = sess.post(url, params=param)
    printJson(resp.json())

def get(url, params):
    resp = sess.get(url, params=param)
    printJson(resp.json())
