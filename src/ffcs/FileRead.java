package ffcs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

/**
 *
 * Programmed By Sanat Rohatgi
 */
/*
 Each object of FileRead class represnts the details of one student.Each obj has a Name ,Year,ID and
 *  arrays of Coursesdone,courseswanted,and grades and teacherswanted for courses to be done
 */
public class FileRead {

    String Name, ID, CoursesDone[], Year, CourseWanted[];
    char Grades[];
    int TeacherChoice[];

    public static void mainfunc(FileRead obj[]) throws Exception {
        int c = 0, k = 0;
        String temp;
        BufferedReader ob1 = new BufferedReader(new FileReader("courseopted1.txt"));
        BufferedReader ob3 = new BufferedReader(new FileReader("history.txt"));
        BufferedReader ob2 = new BufferedReader(new FileReader("courseopted1.txt"));
        k = 0;

        for (k = 0; ((temp = ob2.readLine()) != null); k++) {
            StringTokenizer ss = new StringTokenizer(temp, "_");
            obj[k].Name = ss.nextToken();
            obj[k].ID = ss.nextToken();
            obj[k].Year = ss.nextToken();
            int s = ss.countTokens();
            obj[k].CourseWanted = new String[s];
            obj[k].TeacherChoice = new int[s];
            for (int h = 0; h < s; h++) {
                temp = ss.nextToken();
                obj[k].CourseWanted[h] = temp.substring(0, 6);
                obj[k].TeacherChoice[h] = Integer.parseInt(temp.substring(6));
            }

            temp = ob3.readLine();
            StringTokenizer ss2 = new StringTokenizer(temp, "_");
            int p = 0;
            while (p < 3) {
                String temp2 = ss2.nextToken();
                p++;
            }
            obj[k].Grades = new char[ss2.countTokens()];
            obj[k].CoursesDone = new String[ss2.countTokens()];
            for (int y = 0; y <= ss2.countTokens() + 1; y++) {
                String temp3 = ss2.nextToken();
                if (!(temp3.equals("-"))) {
                    obj[k].CoursesDone[y] = temp3.substring(0, 6);
                    obj[k].Grades[y] = temp3.charAt(6);
                } else {
                    break;
                }
            }
        }
        FileRead tem = new FileRead();
        for (int t = 0; t < obj.length; t++) {                          //Sort Students according to their seniority.
            for (int h = 0; h < obj.length - 1; h++) {
                if ((obj[h].ID).compareTo((obj[h + 1].ID)) < 0) {
                    tem = obj[h];
                    obj[h] = obj[h + 1];
                    obj[h + 1] = tem;
                }
            }
        }
    }
}
