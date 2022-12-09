# CWModularWarfareFixer
Spigot插件，修复指令获取的ModularWarfare枪械无法射击的问题

## 功能

手持ModularWarfare枪械时，检查枪械是否有射击模式NBT
如果没有，就按配置写入一个

## 指令
/cwmwf save <FULL/SEMI> 保存手上枪械的射击模式nbt，此后将按照此nbt设置其他枪械

/cwmwf update 按照枪械Type设置射击模式，一般用不上