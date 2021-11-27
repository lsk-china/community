import inspect
from lib.advpymysql.core.processer import setConnectionData,execute

def setupConnection(prop):
	setConnectionData(prop)

def getParamID(func,name):
	sign = inspect.signature(func)
	try:
		# print(sign.parameters[sign.parameters[name]])
		# return sign.parameters[name]
		index = 0
		arguments = len(sign.parameters)
		code = "sign.bind("
		for i in range(0,arguments):
			code += "\"spam\","
		code = code[:-1]
		code += ")"
		#print(code)
		args = eval(code)
		for arg in args.arguments:
			if arg == name:
				break
			else:
				index += 1
		return index
	except Exception as e:
		print(str(e))
		return None	
		
def getVariables(s0):
	chs = []
	for s1 in s0:
		chs.append(s1)
		if s1 == "#" or s1 == "}":
			chs.append(" ")
	s2 = "".join(chs)
	s3 = s2.split(" ")
	result = []
	for s4 in s3:
		if s4.startswith("{"):
			result.append(s4.replace("{","").replace("}",""))
	return result
def renderSql(sql,vars):
	result = sql
	for k in vars:
		v = vars[k]
		#print(k+":"+v)
		result = result.replace("#{"+k+"}","'"+v+"'")
	print(result)
	return result
	
def readAutoInc(dbName):
	sql = "select auto_increment from information_schema.`TABLES` where table_name='"+dbName+"'"
	return int(execute(sql)[0])

def parseTableNameByInsert(sql):
	s0 = sql.split(" ")
	s1 = s0[2]
	index1 = s1.find("(")
	s2 = s1[:index1]
	return s2
