package com.aim.printerdemo_nosdk;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Ildi on 3/28/2018.
 */
@SuppressLint({"UseSparseArrays"})
public class Table {


        private List<String> tableRows = new ArrayList();
        private String tableReg;
        private HashMap<Integer, String> unPrintColumnMap = new HashMap();
        private int[] tableColWidth;
        private boolean alignRight;

        public Table(String column, String regularExpression, int[] columnWidth) {
            this.tableRows.add(column);
            this.tableReg = regularExpression;
            if(columnWidth != null) {
                this.tableColWidth = columnWidth;
            } else {
                this.tableColWidth = new int[column.split(regularExpression).length];

                for(int i = 0; i < this.tableColWidth.length; ++i) {
                    this.tableColWidth[i] = 8;
                }
            }

        }

        public void setColumnAlignRight(boolean right) {
            this.alignRight = right;
        }

        public void addRow(String row) {
            if(this.tableRows != null) {
                this.tableRows.add(row);
            }

        }

        public String getTableText() {
            StringBuffer sb = new StringBuffer();

            for(int m = 0; m < this.tableRows.size(); ++m) {
                String[] tableLine = ((String)this.tableRows.get(m)).split(this.tableReg);
                sb.append(this.printTableLine(tableLine));
                sb.append("\n");
            }

            return sb.toString();
        }

        private String printTableLine(String[] tableLine) {
            StringBuffer sb = new StringBuffer();
            int sub_length = 0;
            String[] line = tableLine;

            for(int i = 0; i < line.length; ++i) {
                line[i] = line[i].trim();
                int index = line[i].indexOf("\n");
                if(index != -1) {
                    this.unPrintColumnMap.put(Integer.valueOf(i), line[i].substring(index + 1));
                    line[i] = line[i].substring(0, index);
                    sb.append(this.printTableLine(line));
                    sb.append(this.printNewLine(line));
                    return sb.toString();
                }

                int length = getStringCharacterLength(line[i]);
                int col_length = this.tableColWidth.length;
                int col_width = 8;
                if(i < col_length) {
                    col_width = this.tableColWidth[i];
                }

                if(length > col_width && i != line.length - 1) {
                    sub_length = getSubLength(line[i], col_width);
                    this.unPrintColumnMap.put(Integer.valueOf(i), line[i].substring(sub_length, line[i].length()));
                    line[i] = line[i].substring(0, sub_length);
                    sb = new StringBuffer();
                    sb.append(this.printTableLine(line));
                    sb.append(this.printNewLine(line));
                    return sb.toString();
                }

                int j;
                if(i == 0) {
                    sb.append(line[i]);

                    for(j = 0; j < col_width - length; ++j) {
                        sb.append(" ");
                    }
                } else if(this.alignRight) {
                    for(j = 0; j < col_width - length; ++j) {
                        sb.append(" ");
                    }

                    sb.append(line[i]);
                } else {
                    sb.append(line[i]);

                    for(j = 0; j < col_width - length; ++j) {
                        if(i != line.length - 1) {
                            sb.append(" ");
                        }
                    }
                }
            }

            return sb.toString();
        }

        private String printNewLine(String[] oldLine) {
            if(this.unPrintColumnMap.isEmpty()) {
                return "";
            } else {
                StringBuffer sb = new StringBuffer();
                String[] newLine = new String[oldLine.length];
                Iterator iterator = this.unPrintColumnMap.entrySet().iterator();

                while(iterator.hasNext()) {
                    Map.Entry<Integer, String> entry = (Map.Entry)iterator.next();
                    Integer key = (Integer)entry.getKey();
                    String value = (String)entry.getValue();
                    if(key.intValue() < oldLine.length) {
                        newLine[key.intValue()] = value;
                    }
                }

                this.unPrintColumnMap.clear();

                for(int i = 0; i < newLine.length; ++i) {
                    if(newLine[i] == null || newLine[i] == "") {
                        newLine[i] = " ";
                    }
                }

                sb.append(this.printTableLine(newLine));
                return "\n" + sb.toString();
            }
        }


    public static int getStringCharacterLength(String line) {
        int length = 0;

        for(int j = 0; j < line.length(); ++j) {
            if(line.charAt(j) > 256) {
                length += 2;
            } else {
                ++length;
            }
        }

        return length;
    }

    public static int getSubLength(String line, int width) {
        int length = 0;

        for(int j = 0; j < line.length(); ++j) {
            if(line.charAt(j) > 256) {
                length += 2;
            } else {
                ++length;
            }

            if(length > width) {
                int temp = line.substring(0, j - 1).lastIndexOf(" ");
                if(temp != -1) {
                    return temp;
                }

                return j - 1 == 0?1:j - 1;
            }
        }

        return line.length();
    }

}
