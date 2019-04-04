package main.utility;

import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;

import java.io.PrintStream;
import java.util.Iterator;

public class TableUtil {
    public static void print(GridTable g) {
        print(g, System.out);
    }

    public static void print(GridTable g, PrintStream out) {
        g = g.apply(Cell.Functions.TOP_ALIGN).apply(Cell.Functions.LEFT_ALIGN);
        Iterator var2 = g.toCell().iterator();

        while(var2.hasNext()) {
            String line = (String)var2.next();
            out.println(line);
        }

    }

    public static StringBuilder render(GridTable g) {
        g = g.apply(Cell.Functions.TOP_ALIGN).apply(Cell.Functions.LEFT_ALIGN);
        Iterator var2 = g.toCell().iterator();

        StringBuilder sb = new StringBuilder();

        while(var2.hasNext()) {
            String line = (String)var2.next();
            sb.append(line).append("\n");
        }

        return sb;
    }
    public static StringBuilder renderInCodeBlock(GridTable g) {
        return new StringBuilder("```js\n") // removed padding spaces
                .append(render(g))
                .append("```");
    }
}
