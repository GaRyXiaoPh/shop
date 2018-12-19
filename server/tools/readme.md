### 步骤1
python migration-db.py 数据文件目录
这个命令在当前目录生成3个文件user-output.sql,user-bank-output.sql,user-asset-output.sql

### 步骤2 生成后的sql脚本， 用以下命令把insert前面的逗号改成分号
sed -e ':a' -e 'N' -e '$!ba' -e 's/,\nINSERT/;INSERT/g' user-output.sql > user-output.sql.2
sed -e ':a' -e 'N' -e '$!ba' -e 's/,\nINSERT/;INSERT/g' user-bank-output.sql > user-bank-output.sql.2
sed -e ':a' -e 'N' -e '$!ba' -e 's/,\nINSERT/;INSERT/g' user-asset-output.sql > user-asset-output.sql.2

### 步骤3

把*.2文件中最后一个逗号改成分号

### 步骤4

直接通过mysql导入