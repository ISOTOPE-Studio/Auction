#拍卖行插件
	玩家可以自由的在拍卖行交易物品，拍卖行为数据库储存可跨服同步！

##功能
###GUI界面
清楚的显示物品的出售者，价格，数量。以及物品信息（完全显示）和剩余天数

有翻页功能，材质ID是箭头(262)。

玩家可以排列物品，从高低价格或者从低到高。

###上架
玩家输入指令进入拍卖行，之后点击按钮(漏斗材质)进入上架GUI，把要出售的物品放入后会有告示牌提示输入价格

如果格式不对退回到邮箱。

###购买
玩家输入指令进入拍卖行，点击要购买的物品一次后会提示释放确定购买 物品价格XXX，数量XXXX。

再次点击即可购买，购买的物品会发送到邮箱
	
###获得购买的物品
玩家输入指令进入自己的邮箱，直接将邮箱内的物品拖动至自己的背包即可
	
###剩余天数说明
玩家上架的物品只会在拍卖行存在3天，3天过后如果没有出售，就会发送到玩家的邮箱内
	
###获得出售物品的金钱
玩家输入指令进入自己的邮箱，如果物品出售就会变成纸(ID:339)会说明是什么物品被出售了多少钱

只会玩家只需要点击一下钱就会打入自己的账上(无法拖动纸到自己的背包)

拍卖行物品需完整的保存物品NBT信息等。防止被买了后失效等问题。并且GUI界面需要处理好。防止玩家可以直接拿GUI里面的道具等!
	
###下架物品
玩家输入指令进入自己邮箱，点击即可下架物品，下架的物品会发送到自己的邮箱

##命令
`/auction` 查看命令帮助

`/auction market` 查看拍卖行

`/auction mail` 查看邮箱