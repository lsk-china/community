import pymysql
from lib.advpymysql.core.pyProperties import parse
from lib.advpymysql.core.pyBases import *

datas = None

def setConnectionData(data):
	# global conn
	# if conn != None:
	# 	return
	# datas = parse(data)
	# conn = pymysql.connect(datas.get("db.host"),datas.get("db.username"),datas.get("db.password"),datas.get("db.database"))
	global datas
	datas = parse(data)

def execute(sql):
	if datas == null:
		raise Exception("Connection didn't setup.")
	conn = null
	try:
		conn = pymysql.connect(host=datas.get("db.host"),user=datas.get("db.username"),password=datas.get("db.password"),database=datas.get("db.database"))
		cursor = conn.cursor()
		cursor.execute(sql)
		result = cursor.fetchall()
		cursor.close()
		conn.commit()
		conn.close()
		return result
	except Exception as e:
		if not conn == null:
			conn.rollback()
			conn.close()
		raise e
