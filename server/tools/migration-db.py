#coding=utf-8
import uuid
import sys
import glob
import codecs
userTemplate = ""
userBankTemplate = ""
userAssetTemplate = ""
#检查是否有重复会员
mobileList = set()
mobile2UserMap = {}

def processLine(line):
    '''INSERT INTO `baodan2`.`pmw_member` (`id`, `cnname`, `username`, `password`, `type`, `bank`, `bankcard`, `bankaddress`, `tuijian`, `fwz`, `fwcenter`, `bianhao`, `m`)
    VALUES ('1766707', '白永利', '15604780671', 'byl8985', '1', '中国工商银行', '6215590613002500217', '中国工商银行内蒙古自治区巴彦淖市新华东街支行', '839715', '3923', '65', '', '6000');
    '''
    line = line[line.index('VALUES') + 6:];
    values = line.split(', ');

    values = [value.replace("'",'').replace('(','').replace(')','').replace(';','')  for value in values]

    user = {}

    user["userId"] = str(uuid.uuid4());
    user["trueName"] = values[1].strip();
    #电话字段有很多奇葩的值，需要作清洗
    #http://ju.outofmemory.cn/entry/314014
    user["mobile"] = values[2].strip()
    user["mobile"] = user["mobile"].strip(u"：").strip(u"，").strip(u"。").strip(u".").strip(u":").strip(u"+").strip(u",")
    user["mobile"] = user["mobile"].replace("-","").replace(u"手机","").replace(u"电话","").replace("0","0")
    user["mobile"] = user["mobile"].replace(":","").replace("/","").replace("\\","").replace(" ","")
    user["password"] = values[3].strip();
    user["level"] = values[4].strip();
    if user["level"] == "2":
        user["level"] ="3" #2对kt中的站长，
    elif user["level"] == "3":
        user["level"] ="4" #3对kt中的中心主任
    user["bankName"] = values[5].strip().replace("\\","");
    if len(user["trueName"]) > 36:
        user["trueName"] = user["trueName"][0:36]


    user["bankCard"] = values[6].strip().replace("\\","");
    user["openBank"] = values[7].strip().replace("\\","");
    #数据库字段长度是128
    if len(user["openBank"]) > 100:
        user["openBank"] = user["openBank"][0:100]

    if len(user["bankName"]) > 100:
        user["bankName"] = user["bankName"][0:100]

    user["referrer"] = values[8].strip();
    user["pid"] = values[9].strip();
    user['originalId'] = values[0].strip();

    if user["pid"] == "NULL" or user["pid"] == "":
        user["pid"] = values[10].strip();

    point = values[12].strip()
    if point == "" or point == "NULL":
        user["point"] = "0"
    else:
        user["point"] = point


    if user["mobile"] not in mobileList:
        mobile2UserMap[user["originalId"]] = user;



    userAssetOutput = userAssetTemplate.replace("{userId}", user["userId"]).replace("{point}",user["point"]);
    userBankOutput = userBankTemplate.replace("{userId}",user["userId"]).replace("{trueName}",user["trueName"]).replace("{bankName}",user["bankName"]);
    userBankOutput = userBankOutput.replace("{bankCard}",user["bankCard"]).replace("{openBank}",user["openBank"]);

    return user["mobile"],userAssetOutput,userBankOutput


def processFile(fileName):
    print "processing file: " + fileName
    o = codecs.open(fileName, 'r', 'utf-8')

    userBankOutputSql = codecs.open("user-bank-output.sql.1", "a", "utf-8")
    userAssetOutputSql = open("user-asset-output.sql.1",'a')

    for line in o.readlines():
        mobile, userAssetOutput, userBankOutput = processLine(line);
        if mobile.isdigit():
            if mobile not in mobileList:
                userBankOutputSql.write(userBankOutput + "\n")
                userAssetOutputSql.write(userAssetOutput + "\n")

                mobileList.add(mobile)
            else:
                print "duplicated mobile: " + mobile

        else:
            print "mobile is not a digit:" + mobile

def processUserList():
    userOutputSql = codecs.open("user-output.sql.1",'a',"utf-8")

    for userId in mobile2UserMap:
        user = mobile2UserMap[userId]

        if user["mobile"] not in mobileList:
            continue

        userOutput = userTemplate.replace("{userId}",user["userId"]).replace("{mobile}",user["mobile"]).replace("{level}",user["level"])

        userOutput = userOutput.replace("{trueName}",user["trueName"]);

        if user["pid"] is None:
            print "pid not found for: " + userId;
            continue

        if user["pid"] in mobile2UserMap:
            userOutput = userOutput.replace("{pid}", mobile2UserMap[user["pid"]]["userId"]);
        else:
            print "pid not found in map: " + user["pid"]

        if user["referrer"] in mobile2UserMap:
            userOutput = userOutput.replace("{referrer}", mobile2UserMap[user["referrer"]]["userId"]);
        else:
            print "referrer not found in map: " + user["referrer"]


        userOutputSql.write(userOutput + u"\n")

    return ""


if __name__ == "__main__":

    with codecs.open('tb_user_template.sql', 'r') as myfile:
        userTemplate=myfile.read().replace('\n', '')
    with codecs.open('tb_user_bankcard_template.sql', 'r') as myfile:
        userBankTemplate=myfile.read().replace('\n', '')
    with codecs.open('tb_user_asset_template.sql', 'r') as myfile:
        userAssetTemplate=myfile.read().replace('\n', '')


    fileNames = glob.glob(sys.argv[1] + "/*.php")
    for fileName in fileNames:
        processFile(fileName)

    processUserList()

    #1w条语句加入一个insert 语句 （这样导入速度快）

    input = open("user-output.sql.1", 'r')
    userOutputSql = open("user-output.sql",'a')

    lineNumber = 0

    for line in input.readlines():
        if lineNumber % 10000 == 0:
            userOutputSql.write("INSERT INTO tb_user(`id`,username,nationalCode,mobile,password,`salt`,nick,referrer,`level`,status,transactionPassword,avatar,createTime,lastTime,pid,standNo,trueName) VALUES"
                         + "\n")
        lineNumber = lineNumber + 1
        userOutputSql.write(line)


    input = open("user-asset-output.sql.1", 'r')
    userOutputSql = open("user-asset-output.sql",'a')

    lineNumber = 0

    for line in input.readlines():
        if lineNumber % 10000 == 0:
            userOutputSql.write("INSERT INTO tb_user_asset(`id`,userId,currency,withdrawable,spendable,internalAsset,availableBalance,reservedBalance,createTime,lastTime) VALUES"
                         + "\n")
        lineNumber = lineNumber + 1
        userOutputSql.write(line)


    input = open("user-bank-output.sql.1", 'r')
    userOutputSql = open("user-bank-output.sql",'a')

    lineNumber = 0

    for line in input.readlines():
        if lineNumber % 10000 == 0:
            userOutputSql.write("INSERT INTO tb_user_bankcard(`id`,userId,trueName,bankCard,bankName,openBank,createTime,updateTime) VALUES"
                         + "\n")
        lineNumber = lineNumber + 1
        userOutputSql.write(line)




