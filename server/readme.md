＃模块说明
## 管理后台模块 －－ 审核商家

lem-management

## app接口模块 －－ 会员登录app

lem-front

## 商家模块 －－ 商家管理自己的商品

lem-shop

## 第三方物流回调模块 －－ 暂时用不到

lem-open

## 商圈模块 －－ 对商家进行评价，暂时用不到

# 测试环境说明

服务器: 172.20.21.37

目录: /opt/kt-server

## 启动服务
启动app后台： service lem-front start

启动商家后台： service lem-shop start

启动管理后台： service lem-management start

## 日志文件目录:

app后台: /opt/kt-server/front/logs

商家后台: /opt/kt-server/shop/logs

管理后台: /opt/kt-server/management/logs




