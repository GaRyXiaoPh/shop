package cn.kt.mall.test;


import cn.kt.mall.common.util.DateUtil;
import cn.kt.mall.common.util.IDUtil;

import cn.kt.mall.common.util.PasswordUtil;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.util.JdbcConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

//解析旧系统sql文件
public class DbMigration {
    //用来防止重复手机号
    Set<String> mobileSet = new HashSet<>();
    //旧系统的用户id和新系统的用户id之间的映射
    Map<String, String> originalId2NewUserIdMap = new HashMap<>();
    //处理后的行数据
    List<UserLine> lineList =  new ArrayList<>();

    private String outputPath;

    private int threadNumbers = 4;

    public static void main(String[] args) throws IOException, InterruptedException {
        //生成的sql在这个目录下，一共4个文件
        DbMigration migration = new DbMigration("d:/data");

        //把旧系统的sql文件都放到这个目录

        Long start = System.currentTimeMillis();

        File file = new File("d:/data/input");
        for (File f : file.listFiles()) {
            start = System.currentTimeMillis();

            for (String line : Files.lines(Paths.get(f.getPath())).collect(Collectors.toList())) {
                migration.parseLine(line);
            }

            System.out.println("parsed sql file: " + f.getName() + " elapsed time: " + (System.currentTimeMillis() - start)/1000l);
        }



        //生成批插入语句
/*        start = System.currentTimeMillis();

        migration.writeUserBankTable();
        System.out.println("writing user bank table"+ " elapsed time: " + (System.currentTimeMillis() - start)/1000l);

        start = System.currentTimeMillis();

        migration.writeUserAssetTable();
        System.out.println("writing user asset table"+ " elapsed time: " + (System.currentTimeMillis() - start)/1000l);

        start = System.currentTimeMillis();

        migration.writeUserAssetBaseTable();
        System.out.println("writing user asset base table"+ " elapsed time: " + (System.currentTimeMillis() - start)/1000l);

        start = System.currentTimeMillis();

        migration.writeUserCouponTable();
        System.out.println("writing user coupon table"+ " elapsed time: " + (System.currentTimeMillis() - start)/1000l);

        start = System.currentTimeMillis();
        System.out.println(DateUtil.getTime(new Date()) + " start generating password");
        migration.generatePassword();
        System.out.println("generated password"+ " elapsed time: " + (System.currentTimeMillis() - start)/1000l);*/

        start = System.currentTimeMillis();

        migration.writeUserTable();
        System.out.println("writing user table"+ " elapsed time: " + (System.currentTimeMillis() - start)/1000l);

        long end = System.currentTimeMillis();

        System.out.println("" + migration.lineList.size() + " lines processed, elapsed time: " + (end - start)/ 1000l);

    }

    public DbMigration(String outputPath) {
        this.outputPath = outputPath;
    }

    //生成银行卡表的插入语句
    private void writeUserBankTable() throws IOException {
        FileWriter writer = new FileWriter(outputPath + "/user-bank.sql");
        BufferedWriter bw = new BufferedWriter(writer);

        String STATEME_VALUES = "(null,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",NOW(),NOW())";

        for (int i =0 ;i < lineList.size(); ++i) {
            UserLine line = lineList.get(i);
            if (i % 10000 == 0) {
                bw.write("INSERT INTO tb_user_bankcard(`id`,userId,trueName,bankCard,bankName,openBank,createTime,updateTime) VALUES");
            }

            String insertValues = String.format(STATEME_VALUES, line.userId, line.trueName, line.bankCard, line.bankName, line.openBank);

            //1w行一次插入， 这样插入速度快
            if ( i % 10000 == 9999) {
                bw.write(insertValues + ";\n");
            } else if (i != lineList.size() -1){
                bw.write(insertValues + ",\n");
            } else {
                //最后一个语句
                bw.write(insertValues + ";\n");
            }
        }

        bw.close();
        writer.close();
    }

    private void writeUserAssetTable() throws IOException {
        FileWriter writer = new FileWriter(outputPath + "/user-asset.sql");
        BufferedWriter bw = new BufferedWriter(writer);

        String STATEME_VALUES = "(\"%s\",\"%s\",\"%s\",0,0,1,0,\"%s\",NOW(),NOW())";

        for (int i =0 ;i < lineList.size(); ++i) {
            UserLine line = lineList.get(i);
            if (i % 10000 == 0) {
                bw.write("INSERT INTO tb_user_asset(`id`,userId,currency,withdrawable,spendable,internalAsset,availableBalance,reservedBalance,createTime,lastTime) VALUES");
            }

            String insertPopcValues = String.format(STATEME_VALUES, IDUtil.getUUID(), line.userId, "popc",line.popc);
            String insertPointValues = String.format(STATEME_VALUES, IDUtil.getUUID(), line.userId, "point","0");

            //1w行一次插入， 这样插入速度快
            if ( i % 10000 == 9999) {
                bw.write(insertPopcValues + ",\n");
                bw.write(insertPointValues + ";\n");
            } else if (i != lineList.size() -1){
                bw.write(insertPopcValues + ",\n");
                bw.write(insertPointValues + ",\n");
            } else {
                //最后一个语句
                bw.write(insertPopcValues + ",\n");
                bw.write(insertPointValues + ";\n");
            }
        }

        bw.close();
        writer.close();
    }

    private void writeUserAssetBaseTable() throws IOException {
        FileWriter writer = new FileWriter(outputPath + "/user-asset-base.sql");
        BufferedWriter bw = new BufferedWriter(writer);

        String STATEME_VALUES = "(\"%s\",\"%s\",\"%s\",0,0,NOW(),\"%s\",100)";

        for (int i =0 ;i < lineList.size(); ++i) {
            UserLine line = lineList.get(i);
            if (i % 10000 == 0) {
                bw.write("INSERT INTO tb_user_asset_base(`userId`,assetType,reservedBalance,currentReleseNum,sendFinish,createTime,afterAmount,needReleseDays) VALUES");
            }

            String insertPopcValues = String.format(STATEME_VALUES, line.userId, "popc", line.popc, line.popc);
            String insertCouponValues = String.format(STATEME_VALUES, line.userId, "coupon", line.coupon, line.coupon);

            //1w行一次插入， 这样插入速度快
            if ( i % 10000 == 9999) {
                bw.write(insertPopcValues + ",\n");
                bw.write(insertCouponValues + ";\n");
            } else if (i != lineList.size() -1){
                bw.write(insertPopcValues + ",\n");
                bw.write(insertCouponValues + ",\n");
            } else {
                //最后一个语句
                bw.write(insertPopcValues + ",\n");
                bw.write(insertCouponValues + ";\n");
            }
        }

        bw.close();
        writer.close();
    }

    //生成优惠券积分表的插入语句
    private void writeUserCouponTable() throws IOException {
        FileWriter writer = new FileWriter(outputPath + "/user-coupon.sql");
        BufferedWriter bw = new BufferedWriter(writer);

        String STATEME_VALUES = "(\"%s\",\"%s\",0,0,0, NOW(),0,\"%s\",0,\"%s\")";

        String couponId = "87916019-19e1-4383-8539-a7a972134d81";
        for (int i =0 ;i < lineList.size(); ++i) {
            UserLine line = lineList.get(i);
            if (i % 10000 == 0) {
                bw.write("INSERT INTO tb_user_coupon(`id`,userId,cdkeyNum,couponType,couponNum,createTime,status,couponId,currentExtractNum,reservedCouponNum) VALUES");
            }

            String insertValues = String.format(STATEME_VALUES, IDUtil.getUUID(),line.userId, couponId, line.coupon);

            //1w行一次插入， 这样插入速度快
            if ( i % 10000 == 9999) {
                bw.write(insertValues + ";\n");
            } else if (i != lineList.size() -1){
                bw.write(insertValues + ",\n");
            } else {
                //最后一个语句
                bw.write(insertValues + ";\n");
            }
        }

        bw.close();
        writer.close();
    }

    private void generatePassword() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threadNumbers);

        CountDownLatch c = new CountDownLatch(threadNumbers);
        AtomicInteger countsProcessed = new AtomicInteger(0);

        for (int i = 0 ;i < threadNumbers; ++i) {
            pool.execute(new GeneratePassword(threadNumbers, c, lineList,i,countsProcessed));
        }

        synchronized (c) {
            c.await();
        }

    }

    private static class GeneratePassword implements Runnable {
        private int number;
        List<UserLine> lineList =  new ArrayList<>();
        CountDownLatch c;
        int threadNumbers;
        AtomicInteger countsProcessed;


        public GeneratePassword(int threadNumbers,CountDownLatch c, List<UserLine> lineList, int number, AtomicInteger countsProcessed) {
            this.number = number;
            this.lineList = lineList;
            this.c = c;
            this.threadNumbers =threadNumbers;
            this.countsProcessed = countsProcessed;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < lineList.size(); ++i) {
                    if (i% threadNumbers == number) {
                        UserLine line =  lineList.get(i);
                        String[] pns = PasswordUtil.getEncryptPassword(line.password);
                        line.newPassword = pns[0];
                        line.salt = pns[1];
                        int counts = countsProcessed.incrementAndGet();
                        if (counts % 10000 == 0) {
                            System.out.println(DateUtil.getTime(new Date()) + "generating password at line: " + counts);
                        }
                    }
                }
            } catch (Exception e) {

            } finally {
                c.countDown();
            }

        }
    }


    private void writeUserTable() throws IOException {
        FileWriter writer = new FileWriter(outputPath + "/user.sql");
        BufferedWriter bw = new BufferedWriter(writer);

        String STATEME_VALUES = "(\"%s\",\"%s\",\"1\",\"%s\",\"%s\",\"%s\",\"\",\"%s\",\"%d\",0,null, null, \"%s\", \"%s\",\"%s\",\"%s\", \"%s\", \"%s\")";

        for (int i =0 ;i < lineList.size(); ++i) {
            UserLine line = lineList.get(i);
            if (i % 10000 == 0) {
                bw.write("INSERT INTO tb_user(`id`,username,nationalCode,mobile,password,`salt`,nick,referrer,`level`,status,transactionPassword,avatar,createTime,lastTime,pid,standNo,trueName,xin) VALUES");
            }

            String referrerId = originalId2NewUserIdMap.get(line.referrerId);
            String parentUserId = originalId2NewUserIdMap.get(line.parentUserId);


            String insertValues = String.format(STATEME_VALUES, line.userId, line.mobile, line.mobile,
                    line.newPassword, line.salt,
                    referrerId == null ? "" : referrerId, line.level, line.registerTime, line.registerTime,
                    parentUserId == null ? "" : parentUserId, line.stationNo,line.trueName,line.xin);

            //1w行一次插入， 这样插入速度快
            if ( i % 10000 == 9999) {
                bw.write(insertValues + ";\n");
            } else if (i != lineList.size() -1){
                bw.write(insertValues + ",\n");
            } else {
                //最后一个语句
                bw.write(insertValues + ";\n");
            }
        }

        bw.close();
        writer.close();
    }



    //解析原来的sql文件的一行,内容如下:
    // "INSERT INTO `baodan2`.`pmw_member` (`id`, `cnname`, `username`, `password`, `type`, `bank`, " +
    // "`bankcard`, `bankaddress`, `tuijian`, `fwz`, `fwcenter`, `bianhao`, `m`, `posttime`) " +
    // "VALUES ('16', 'xxx（xxx）（17329327685）', '13653624741', 'LjL901216', '3', '中国银行'," +
    // " '6666', '深圳市罗湖区支行', NULL, NULL, '16', '3001', '1035250.76', '1483408493');";
    private  void parseLine(String line) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(line,  JdbcConstants.MYSQL);

        if (stmtList.isEmpty()) {
            System.out.println("failed to parse: " + line);
            return;
        }

        SQLStatement statement = (SQLStatement)stmtList.get(0);
        if (!(statement instanceof MySqlInsertStatement) ){
            System.out.println("not a insert statement: " + line);
            return;
        }

        MySqlInsertStatement insertStatement = (MySqlInsertStatement)statement;

        List<SQLExpr> values = insertStatement.getValues().getValues();
        UserLine userLine = new UserLine();
        userLine.userId = IDUtil.getUUID();
        userLine.originalUserId = clean(values.get(0).toString());
        userLine.trueName = parseTrueName(clean(values.get(1).toString()));
        userLine.mobile = parseMobile(clean(values.get(2).toString()));
        userLine.password = clean(values.get(3).toString());
        userLine.level = parseLevel(Integer.valueOf(clean(values.get(4).toString())));
        userLine.bankName =pargetBankName(clean(values.get(5).toString()));
        userLine.bankCard = clean(values.get(6).toString());
        userLine.openBank = parseOpenbank(clean(values.get(7).toString()));
        userLine.referrerId = clean(values.get(8).toString());
        userLine.parentUserId = clean(values.get(9).toString());
        if (userLine.parentUserId.equals("") || userLine.parentUserId.equals("NULL")) {
            userLine.parentUserId = clean(values.get(10).toString());
        }

        userLine.stationNo = clean(values.get(11).toString());
        //旧系统的积分
        BigDecimal coupon = parseCoupon(clean(values.get(12).toString()));
        //新系统中
        userLine.popc = coupon.multiply(new BigDecimal("0.5")).toPlainString();
        userLine.coupon = coupon.toPlainString();
        //注册时间，单位秒
        userLine.registerTime = DateUtil.getTime(new Date(Integer.parseInt(clean(values.get(13).toString())) * 1000l));

        //有很多电话是重复注册的，需要过滤掉
        if (!mobileSet.contains(userLine.mobile)) {
            //从一行语句从解析出系统需要的4个表的数据

            mobileSet.add(userLine.mobile);
            originalId2NewUserIdMap.put(userLine.originalUserId, userLine.userId);
            lineList.add(userLine);
        }
        userLine.xin = clean(values.get(14).toString());
    }

    private static class UserLine {
        //新系统的id
        String userId;
        //旧系统的id
        String originalUserId;
        String trueName;
        String mobile;
        String password ;
        String newPassword;
        String salt;
        Integer level ;
        String bankName ;
        String bankCard;
        String openBank ;
        String referrerId ;
        String parentUserId ;
        //新系统的popc值
        String popc;
        //新系统的 彩票积分
        String coupon;
        //注册时间，旧系统是unix时间戳, 转成yyy-dd-mm hh:mm:ss格式字符串
        String registerTime ;
        //站点编号
        String stationNo;
        //信用分
        String xin;
    }
    //对数据做最基本的处理
    private String clean(String line) {
        return line.trim().replace("'","")
                .replace("(","")
                .replace(")","")
                .replace(";","")
                .replace("\\","")
                .replace("NULL","");

    }

    //旧sql的username字段是电话号码，但是格式很乱，需要过滤掉特殊字符
    private String parseMobile(String mobile) {
        return mobile.replace("：","")
                .replace("，","")
                .replace("。","")
                .replace(".","").replace(":","")
                .replace("+","").replace(",","")
                .replace("-","").replace("手机","")
                .replace("电话","").replace("O","0")
                .replace(":","").replace(" ","");

    }

    //旧sql的姓名字段有很多类似  袁理（15152168717）这样的写法，只需要前面的姓名
    private String parseTrueName(String trueName) {
        if(trueName.trim().length() == 0 || trueName.trim() == ""){
            trueName = "  ";
        }else{
            trueName = trueName.split(" ")[0];
        }
        //数据长度是36
        return trueName.length() > 36 ? trueName.substring(0,36): trueName;
    }

    private String parseOpenbank(String openbank) {
        return openbank.length() > 120 ? openbank.substring(0,120) : openbank;
    }

    private String pargetBankName(String bankName) {
        return bankName.length() > 120 ? bankName.substring(0,120) : bankName;
    }

    private Integer parseLevel(Integer level) {
        if (level == 2) {
            return 3;
        }

        if (level == 3) {
            return 4;
        }

        return level;
    }

    private BigDecimal parseCoupon(String coupon) {
        if (StringUtils.isBlank(coupon)) {
            return new BigDecimal("0");
        }

        //popc只转50%
        return new BigDecimal(coupon);
    }
}
