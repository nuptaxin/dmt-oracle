Data Migration Tool for Oracle
===================

数据迁移工具(Oracle版)，以Oracle官方提供DMP文件为中间文件，提供Oracle数据库之间的数据转移功能。本程序只生成相关的数据操作脚本，由于数据库的配置及其它的差异，不能保证数据的导入和导出过程完全正常。请在执行相关bat脚本前，确认脚本内批处理命令的正确性。

版本变更日志：
- 0.1.0.0
	- 时间：2016年07月29日
	- 第一版发布版本。
- 0.2.0.0
	- 时间：2016年09月01日
	- 修复：weblaf与原项目的依赖冲突；Spring默认扫描包名错误导致imp无法读取配置文件；