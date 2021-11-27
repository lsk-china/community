from functools import wraps
from lib.advpymysql.core.tools import *
from lib.advpymysql.core.processer import execute
from lib.pytools.pyBases import *

def Select(sql="", returnFirst=false):
	def doDecorator(func):
		@wraps(func)
		def run(*args,**kwargs):
			argList = getVariables(sql)
			vars = {}
			for arg in argList:
				index = getParamID(func,arg)
				#print(arg+":"+str(args[index]))
				vars[arg] = str(args[index])
			#print(vars)
			dbResult = execute(renderSql(sql,vars))
			if returnFirst:
				return dbResult[0]
			else:
				return dbResult
		return run
	return doDecorator
	
def Insert(sql="", readID=false):
	def doDecorator(func):
		@wraps(func)
		def run(*args,**kwargs):
			argList = getVariables(sql)
			vars = {}
			for arg in argList:
				index = getParamID(func,arg)
				#print(arg+":"+str(args[index]))
				vars[arg] = str(args[index])
			#print(vars)
			execute(renderSql(sql,vars))
			if readID:
				table = parseTableNameByInsert(sql)
				return execute("select max(id) from "+table)[0][0]
		return run
	return doDecorator
	
def Update(sql=""):
	def doDecorator(func):
		@wraps(func)
		def run(*args,**kwargs):
			argList = getVariables(sql)
			vars = {}
			for arg in argList:
				index = getParamID(func,arg)
				# print(index)
				vars[arg] = str(args[index])
			#print(vars)
			return execute(renderSql(sql,vars))
		return run
	return doDecorator
	
def Delete(sql=""):
	def doDecorator(func):
		@wraps(func)
		def run(*args,**kwargs):
			argList = getVariables(sql)
			vars = {}
			for arg in argList:
				index = getParamID(func,arg)
				#print(arg+":"+str(args[index]))
				vars[arg] = str(args[index])
			#print(vars)
			return execute(renderSql(sql,vars))
		return run
	return doDecorator

def LockTableWrite(table):
	def doDecorator(func):
		@wraps(func)
		def run(*args, **kwargs):
			execute("lock tables "+table+" write;")
			result = func(*args, **kwargs)
			execute("unlock tables;")
			return result
		return run
	return doDecorator

