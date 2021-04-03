package org.pecker.sql.parse;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.util.JdbcConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsqlParse {

    public static class ExportTableAliasVisitor extends MySqlASTVisitorAdapter {
        private Map<String, SQLTableSource> aliasMap = new HashMap<String, SQLTableSource>();
        public boolean visit(SQLExprTableSource x) {
            String alias = x.getAlias();
            aliasMap.put(alias, x);
            return true;
        }

        public boolean visit(SQLSelect x) {
            String alias = x.toString();
            System.out.println(alias);
            return true;
        }

        public boolean visit(MySqlUpdateStatement x) {
            return true;
        }

        public boolean visit(MySqlInsertStatement x) {
            return true;
        }

        public Map<String, SQLTableSource> getAliasMap() {
            return aliasMap;
        }
    }

    public static void main(String[] args) {
        String sql = "select case when a.o=1 then 1 when a.o=2 then 2 else 3 end from t a join m b on a.w=b.w where a.y=1 and b.k=2 and a.u=3;" +
                "update at a join bt b on a.id= b.aid  set a.x=1 where b.h=2; " +
                "insert into at(h,j,k) values(1,2,3) on duplicate key update h=1,j=2,k=3;";
        ExportTableAliasVisitor visitor = new ExportTableAliasVisitor();
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        for (SQLStatement sqlStatement : stmtList){
            sqlStatement.accept(visitor);
        }
        SQLTableSource tableSource = visitor.getAliasMap().get("a");
        System.out.println(tableSource);
    }
}
