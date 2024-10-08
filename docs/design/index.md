```plantuml
@startuml

!include ./eventstorming/single.puml

scale 1.5

DomainEvent("BookPutInStorage") [
新书已入库
--fields--
二维码
isbn
]

DomainEvent("BookPutOnShelf") [
书已上架
--fields--
二维码
]

DomainEvent("BookRemovedFromShelf") [
书已下架
--fields--
二维码
]

DomainEvent("BookReserved") [
书已被预定
--fields--
预定id
二维码
预订人：userId
预定时间
]


DomainEvent("BookReserveTimeOut") [
预定已超时
--fields--
预定id
二维码
预订人：userId
]

DomainEvent("BookReserveCanceled") [
预定已取消
--fields--
预定id
二维码
预订人：userId
]

DomainEvent("BookBorrowedOut") [
书已被借出
--fields--
借书id
二维码
借出人: userId
借出时间
]

DomainEvent("BookReturned") [
书已被归还
--fields--
借书id
二维码
借出人: userId
归还前已遗失
]

DomainEvent("BookLost") [
书已被遗失
--fields--
借书id
二维码
借书人: userId
]

DomainEvent("BookReturnTimeOut") [
还书已逾期
--fields--
借书id
借书人: userId
二维码
]

DomainEvent("UserAccountSuspended") [
会员账户已暂停
--fields--
userId
]

DomainEvent("UserAccountResumed") [
会员账户已启用
--fields--
userId
]

Person("Admin") [
管理员
]

Person("Member") [
会员
]

Person("Timer") [
定时器
]

Command("PutBookInStorage") [
入库新书
--rule--
1. 二维码不能重复
--fields--
二维码
isbn
--事件--
新书已入库
]

Command("PutBookOnShelf") [
上架
--rule--
1. 书存在且已下架才能上架
--fields--
二维码
--事件--
书已上架
]



Command("RemoveBookFromShelf") [
下架
--rule--
1.书存在且已上架才能下架
--fields--
二维码
--事件--
书已下架
]


Command("ReserveBook") [
预定
--rule--
1.有可预定的书
2.会员账户没有被暂停
3.会员已预定或者借出的书小于3本
4.从可预订的指定ISBN的书里随机选取
--fields--
isbn
预订人：userId
--事件--
书已被预定
]

Command("ReserveTimeOut") [
超时预定
--rule--
1.当前时间离预定时间超过24小时
--fields--
预定id
--事件--
预定已超时
]

Command("CancelReserve") [
取消预定
--rule--
1.有预定，且在24小时内
--fields--
预定id
--事件--
预定已取消
]

FacadeCommand("BorrowOut") [
借出书
--rule--
1.会员账号没有被暂停
2.书已上架，没有被借出，没有被预定
3.会员已预定或者借出的书小于3本
4.书的二维码存在
--fields--
借书人：userId
二维码
--事件--
书已被借出
]

Command("ReturnBook") [
还书
--rule--
1.书的二维码存在
2.书已被借出或者遗失
3.归还已遗失的书，如果时间过了1个月，也算逾期
--fields--
二维码
--事件--
书已被归还
还书已逾期
]



Command("ReportLost") [
上报遗失
--rule--
1.书存在，被借出
2.遗失的书自动下架
--fields--
借书ID
--事件--
书已被遗失
]

Command("TimeOutBorrowing") [
逾期
--rule--
1.书被借出，且1个月未还
2.增加借书会员的逾期次数
--fields--
借书ID
--事件--
还书已逾期
]



Command("ResumeAccount") [
启用会员账户
--rule--
1.会员账户当前被暂停才能启用
--fields--
userId
]

Admin -down-> PutBookInStorage

Admin -down-> PutBookOnShelf

Admin -down-> RemoveBookFromShelf

Member -down-> ReserveBook

Member -down-> CancelReserve

Timer -down-> ReserveTimeOut

Member -down-> BorrowOut

Member -down-> ReturnBook

Member -down-> ReportLost

Timer -down-> TimeOutBorrowing

Admin -down-> ResumeAccount

Policy("SuspendAccountWhenTimeOut") [
增加账户逾期次数
]

Command("IncreaseAccountTimeOutCount") [
增加逾期次数
--rule--
1.会员累计逾期达到3次暂停账户
2.会员账户当前是启用才能被暂停
--fields--
userId
]

BookReturnTimeOut -down-> SuspendAccountWhenTimeOut
SuspendAccountWhenTimeOut -down-> IncreaseAccountTimeOutCount

Policy("RemoveBookWhenLost") [
下架已遗失书
]

BookLost -down-> RemoveBookWhenLost

RemoveBookWhenLost -down-> RemoveBookFromShelf

Policy("PutShelfLostBook") [
上架被找回的遗失的书
--rule--
1.书被借出后遗失
]


BookReturned -down-> PutShelfLostBook

PutShelfLostBook -down-> PutBookOnShelf


Policy("CancelReserveWhenRemoveFromShelf") [
下架书时取消预定
]

BookRemovedFromShelf -down-> CancelReserveWhenRemoveFromShelf

CancelReserveWhenRemoveFromShelf --up--> CancelReserve

Aggregate("UserAgg") [
用户
--id--
userId
--fields--
是否暂停
累计逾期次数
]



ResumeAccount -down-> UserAgg

IncreaseAccountTimeOutCount -down-> UserAgg

UserAgg -down-> UserAccountResumed

UserAgg -down-> UserAccountSuspended



DomainEvent("UserReturnBookTimeOut") [
用户还书已逾期
--fields--
userId
]



UserAgg -down-> UserReturnBookTimeOut

Aggregate("BookAgg") [
书
--id--
二维码
--fields--
isbn
是否上架
]



BookAgg -down-> BookPutInStorage

BookAgg -down-> BookPutOnShelf

BookAgg -down-> BookRemovedFromShelf

PutBookInStorage -down-> BookAgg

RemoveBookFromShelf -down-> BookAgg

PutBookOnShelf -down-> BookAgg



Aggregate("UserOcuppyBookAgg") [
用户占用书
--id--
userId
占用数量
--methods--
获取用户占用书量
]



Policy("modifyUserOcuppyBookPolicy") [
用户占用书规则
--rules--
借出、预定占用书增加
归还、取消预定、预定超时、遗失时占用书减少
]



BookReserved --down--> modifyUserOcuppyBookPolicy

BookReserveTimeOut --down--> modifyUserOcuppyBookPolicy

BookReserveCanceled --down--> modifyUserOcuppyBookPolicy

BookBorrowedOut --down--> modifyUserOcuppyBookPolicy

BookReturned --down--> modifyUserOcuppyBookPolicy

BookLost --down--> modifyUserOcuppyBookPolicy



Command("ModifyUserOcuppyBook") [
调整用户占书量
--fields--
userId
调整值
]



modifyUserOcuppyBookPolicy --down--> ModifyUserOcuppyBook
ModifyUserOcuppyBook -down-> UserOcuppyBookAgg

DomainEvent("UserOcuppyBookChanged") [
用户占用书变化了
--fields--
userId
新占用量
旧占用量
]

UserOcuppyBookAgg -down-> UserOcuppyBookChanged

Aggregate("ReserveAgg") [
预定
--id--
预定id
--fields-
预定时间
预定人
二维码
是否有效
]



Aggregate("BorrowingAgg") [
借出
--id--
借书ID
--fields--
二维码
上报过遗失
是否待还书
借出时间
]





ReserveAgg -down-> BookReserved

ReserveAgg -down-> BookReserveTimeOut

ReserveAgg -down-> BookReserveCanceled


BorrowingAgg -down-> BookBorrowedOut

BorrowingAgg -down-> BookReturned

BorrowingAgg -down-> BookLost

BorrowingAgg -down-> BookReturnTimeOut


ReserveBook -down-> ReserveAgg
ReserveTimeOut -down-> ReserveAgg
CancelReserve -down-> ReserveAgg

ReturnBook -down-> BorrowingAgg
ReportLost -down-> BorrowingAgg
TimeOutBorrowing -down-> BorrowingAgg

Aggregate("AvailableBooksAgg") [
可预订书
--id--
isbn
--fileds--
书二维码集合
--methods--
是否包含书
]



Command("AddToAvailableBooksCmd") [
添加
--fields--
二维码
isbn
]

Command("RemoveFromAvailableBooksCmd") [
移除
--fields--
二维码
isbn
]



DomainEvent("BookAddedToAvailableEvt") [
书已可预定
--fileds--
isbn
二维码
]

DomainEvent("BookRemovedFromAvailableEvt") [
书已不可预定
--fileds--
isbn
二维码
]



AddToAvailableBooksCmd -down-> AvailableBooksAgg

RemoveFromAvailableBooksCmd -down-> AvailableBooksAgg

AvailableBooksAgg -down-> BookAddedToAvailableEvt

AvailableBooksAgg -down-> BookRemovedFromAvailableEvt



Policy("AddAvailablePolicy") [
添加可预订策略
]



Policy("RemoveAvailablePolicy") [
移除可预订策略
]



AddAvailablePolicy -down-> AddToAvailableBooksCmd

RemoveAvailablePolicy -down-> RemoveFromAvailableBooksCmd



BookPutOnShelf -down-> AddAvailablePolicy

BookReserveTimeOut -down-> AddAvailablePolicy

BookReserveCanceled -down-> AddAvailablePolicy

BookReturned -down-> AddAvailablePolicy



BookRemovedFromShelf -down-> RemoveAvailablePolicy

BookReserved -down-> RemoveAvailablePolicy

BookBorrowedOut -down-> RemoveAvailablePolicy



Service("BorrowService") [
借书
--rule--
书可预定则可借出
书已预定时借书人是预定则可借
]

BorrowOut -down-> BorrowService

BorrowService -down-> ReserveAgg : 按二维码查找

BorrowService -down-> AvailableBooksAgg : 是否包含书


Command("BorrowOutCmd") [
借出书
--fields--
借书人ID
二维码
]

BorrowService -down-> BorrowOutCmd
BorrowOutCmd -down-> BorrowingAgg

@enduml
```
