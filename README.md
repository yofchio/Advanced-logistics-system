客服服务中心 Customer Service Centre
调度中心 Dispatch Centre
库房管理 Warehouse management Centre
分站管理中心 Substation management center
配送管理中心 Distribution management center
登录管理 user-centre
网关管理 Gateway-centre
财务管理 Financial-management

mybatis plus 常用方法
【通用条件：】
【比较大小： ( =, <>, >, >=, <, <= )】
eq(R column, Object val); // 等价于 =，例: eq("name", "老王") ---> name = '老王'
ne(R column, Object val); // 等价于 <>，例: ne("name", "老王") ---> name <> '老王'
gt(R column, Object val); // 等价于 >，例: gt("name", "老王") ---> name > '老王'
ge(R column, Object val); // 等价于 >=，例: ge("name", "老王") ---> name >= '老王'
lt(R column, Object val); // 等价于 <，例: lt("name", "老王") ---> name < '老王'
le(R column, Object val); // 等价于 <=，例: le("name", "老王") ---> name <= '老王'

【范围：（between、not between、in、not in）】
between(R column, Object val1, Object val2); // 等价于 between a and b, 例： 			between("age", 18, 30) ---> age between 18 and 30
notBetween(R column, Object val1, Object val2); // 等价于 not between a and b, 例： 	notBetween("age", 18, 30) ---> age not between 18 and 30
in(R column, Object... values); // 等价于 字段 IN (v0, v1, ...),例: in("age",{1,2,3}) ---> age in (1,2,3)
notIn(R column, Object... values); // 等价于 字段 NOT IN (v0, v1, ...), 例: notIn("age",{1,2,3}) ---> age not in (1,2,3)
inSql(R column, Object... values); // 等价于 字段 IN (sql 语句), 例: inSql("id", "select id from table where id < 3") ---> id in (select id from table where id < 3)
notInSql(R column, Object... values); // 等价于 字段 NOT IN (sql 语句)

【模糊匹配：（like）】
like(R column, Object val); // 等价于 LIKE '%值%'，例: like("name", "王") ---> name like '%王%'
notLike(R column, Object val); // 等价于 NOT LIKE '%值%'，例: notLike("name", "王") ---> name not like '%王%'
likeLeft(R column, Object val); // 等价于 LIKE '%值'，例: likeLeft("name", "王") ---> name like '%王'
likeRight(R column, Object val); // 等价于 LIKE '值%'，例: likeRight("name", "王") ---> name like '王%'

【空值比较：（isNull、isNotNull）】
isNull(R column); // 等价于 IS NULL，例: isNull("name") ---> name is null
isNotNull(R column); // 等价于 IS NOT NULL，例: isNotNull("name") ---> name is not null

【分组、排序：（group、having、order）】
groupBy(R... columns); // 等价于 GROUP BY 字段, ...， 例: groupBy("id", "name") ---> group by id,name
orderByAsc(R... columns); // 等价于 ORDER BY 字段, ... ASC， 例: orderByAsc("id", "name") ---> order by id ASC,name ASC
orderByDesc(R... columns); // 等价于 ORDER BY 字段, ... DESC， 例: orderByDesc("id", "name") ---> order by id DESC,name DESC
having(String sqlHaving, Object... params); // 等价于 HAVING ( sql语句 )， 例: having("sum(age) > {0}", 11) ---> having sum(age) > 11

【拼接、嵌套 sql：（or、and、nested、apply）】
or(); // 等价于 a or b， 例：eq("id",1).or().eq("name","老王") ---> id = 1 or name = '老王'
or(Consumer<Param> consumer); // 等价于 or(a or/and b)，or 嵌套。例: or(i -> i.eq("name", "李白").ne("status", "活着")) ---> or (name = '李白' and status <> '活着')
and(Consumer<Param> consumer); // 等价于 and(a or/and b)，and 嵌套。例: and(i -> i.eq("name", "李白").ne("status", "活着")) ---> and (name = '李白' and status <> '活着')
nested(Consumer<Param> consumer); // 等价于 (a or/and b)，普通嵌套。例: nested(i -> i.eq("name", "李白").ne("status", "活着")) ---> (name = '李白' and status <> '活着')
apply(String applySql, Object... params); // 拼接sql（若不使用 params 参数，可能存在 sql 注入），例: apply("date_format(dateColumn,'%Y-%m-%d') = {0}", "2008-08-08") ---> date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")
last(String lastSql); // 无视优化规则直接拼接到 sql 的最后，可能存若在 sql 注入。
exists(String existsSql); // 拼接 exists 语句。例: exists("select id from table where age = 1") ---> exists (select id from table where age = 1)

【QueryWrapper 条件：】
select(String... sqlSelect); // 用于定义需要返回的字段。例： select("id", "name", "age") ---> select id, name, age
select(Predicate<TableFieldInfo> predicate); // Lambda 表达式，过滤需要的字段。
lambda(); // 返回一个 LambdaQueryWrapper

【UpdateWrapper 条件：】
set(String column, Object val); // 用于设置 set 字段值。例: set("name", null) ---> set name = null
etSql(String sql); // 用于设置 set 字段值。例: setSql("name = '老李头'") ---> set name = '老李头'
lambda(); // 返回一个 LambdaUpdateWrapper 


sentinel:
resource：资源名称
limitApp：来源应用
grade：阀值类型，0：线程数，1：QPS
count：单机阀值
strategy：流控模式，0：直接，1：关联，2：链路
controlBehavior：流控效果，0：快速失败，1：warmUp，2：排队等待
clusterMode：是否集群
