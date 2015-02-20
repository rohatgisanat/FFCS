package ffcs;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JTextField;

public class Allocation extends javax.swing.JFrame {

    Allocation Courses[];   //Each object of this array sotres the details of the courses
    FileRead StudentDetails[];          //Array of objects of FileRead class
    String CouseId, Prerequisite;
    Linkedlist RollList1, RollList2;            //Linked List to join the students under the teacher
    node Teacher1, Teacher2;
    String temp = "";
    int AllotmentFlag = 0, DeleteFlag = 0;

    public Allocation(String a1, String a, String b, String c) {
        CouseId = a1;
        Prerequisite = a;
        RollList1 = new Linkedlist();
        Teacher1 = new node(b);
        RollList2 = new Linkedlist();
        Teacher2 = new node(c);

    }    //constructor to assign values to data

    Allocation() throws FileNotFoundException, Exception {
        initComponents();
        callmethods();
    }

    void callmethods() throws FileNotFoundException, IOException, Exception {   //Initial Information is displayed
        readfiles();
        teachersavailable();
        CoursePage();
        history();
        courseopted();
        

    }

    void readfiles() throws FileNotFoundException, IOException, Exception {
        int c = 0;
        String t = "";
        BufferedReader b = new BufferedReader(new FileReader("courseopted1.txt"));
        c = 0;
        while ((t = b.readLine()) != null) {
            c++;                                    //count the number of students
        }
        StudentDetails = new FileRead[c];                //initializing the size of student array
        for (int g = 0; g < c; g++) {
            StudentDetails[g] = new FileRead();
        }
        FileRead.mainfunc(StudentDetails); //reads files and stores them in arrays
    }       //Read and process files and store them

    void CoursePage() {           // Diplays info on the courses  available
        String temp = "", t = "";
        //BufferedReader TeacherFileReader = null;
       
         //   TeacherFileReader = new BufferedReader(new FileReader("teacherinputfile.txt"));
            t = t + ("<table border=\"2\"> <tr><td><b> COURSE ID</b></td> <td><b>PREREQUISITE</b></td></tr> ");
        //while ((temp = TeacherFileReader.readLine()) != null) {
        for (Allocation Course : Courses) {
            StringTokenizer er = new StringTokenizer(temp, "_");
            t += ("<tr><td>" + Course.CouseId + "</td><td>" + Course.Prerequisite + "</td></tr>");
        }
            t = "<html>" + t + "</table></html>";
            jEditorPane1.setText(t);
        

    }

    void history() {
        String t = "";

        t += "<table border=\"2\"><tr><td><b>NAME</b></td><td><b>ID</b></td><td><b>YEAR</b></td><td><b>GRADE</b></td></tr>";
        for (FileRead StudentDetail : StudentDetails) {
            t += ("<tr><td>" + StudentDetail.Name + "</td><td>" + StudentDetail.ID + "</td><td>" + StudentDetail.Year + "</td>"); //Display history store in the FileRead array
            for (int u = 0; u < StudentDetail.Grades.length; u++) {
                t += (StudentDetail.CoursesDone[u] + "&nbsp;&nbsp;&nbsp;&nbsp;" + StudentDetail.Grades[u] + "<br>");
            }
            t += ("</td></tr>");
        }
        t = "<html>" + t + "</table></html>";
        jEditorPane2.setText(t);

    }       //Display Student History

    void courseopted() {
        String temp = "";

        temp += ("<table border=\"2\"><tr><td><b>NAME</b></td><td><b>ID</b></td><td><b>YEAR</b></td><td><b>COURSES OPTED</b></td></tr>");

        for (FileRead StudentDetail : StudentDetails) {
            temp += ("<tr><td>" + StudentDetail.Name + "</td><td>" + StudentDetail.ID + "</td><td>" + StudentDetail.Year + "</td><td> ");
            for (String CourseWanted : StudentDetail.CourseWanted) {
                temp += CourseWanted + "<br>";
            }
        }
        temp += ("</td></tr>");
        temp = "<html>" + temp + "</table></html>";
        jEditorPane3.setText(temp);


    }    //Display the courses wanted by the students using the Student array

    void teachersavailable() throws FileNotFoundException, IOException {      // Reads  and displays the Teachers taking classes
        String t = "", tmp = "", course, prereq, teacher1, teacher2;
        int c = 0;
        t = "<table border=\"2\"><thead><tr><td><b>COURSES</b></td><td><b>PREREQUISITE</b></td><td><b>TEACHER 1</b></td><td><b>TEACHER 2</b></td></tr></thead>";
        BufferedReader noreadteacher = new BufferedReader(new FileReader("teacherinputfile.txt"));
        while ((tmp = noreadteacher.readLine()) != null) {
            c++;                                                //count the no. of courses
        }
        Courses = new Allocation[c];            //Storing courses in array to use later for the student allotment
        BufferedReader readteacher = new BufferedReader(new FileReader("teacherinputfile.txt"));
        for (int j = 0; (tmp = readteacher.readLine()) != null; j++) {
            StringTokenizer techer = new StringTokenizer(tmp, "_");
            course = techer.nextToken();
            prereq = techer.nextToken();
            teacher1 = techer.nextToken();
            if (j % 4 == 0) {
                teacher2 = techer.nextToken();
            } else {
                teacher2 = "  ";
            }
            Courses[j] = new Allocation(course, prereq, teacher1, teacher2);
            t += "<tr><td>" + course + "</td><td>" + prereq + "</td><td>" + teacher1 + "</td><td>" + teacher2 + "</td></tr>";
        }
        t = "<html>" + t + "</table></html>";
        jEditorPane4.setText(t);
    }

    void alloted() throws FileNotFoundException, IOException, Exception { //Allot Students their courses
        String temp, e, f;
        
        int h = 0;

        for (FileRead StudentDetail : StudentDetails) {
            for (int r = 0; r < StudentDetail.CourseWanted.length; r++) {
                e = StudentDetail.CourseWanted[r];
                inner:
                for (int q = 0; q < 30; q++) {
                    f = Courses[q].CouseId;
                    if (e.equals(f)) {
                        //Check if prereq re completed and the Grade N is not awarded
                        for (h = 0; h < StudentDetail.CoursesDone.length; h++) {
                            if (((Courses[q].Prerequisite).equals(StudentDetail.CoursesDone[h]) && StudentDetail.Grades[h] != 'N') || Courses[q].Prerequisite.equals("NULL")) {
                                Courses[q].insert(StudentDetail.ID, StudentDetail.TeacherChoice[r]); //insert into the teacher linkedlist
                                break inner;
                            }
                        }
                    }
                }
            }
        }

        temp = "<tr><td><b>TEACHERS</b></td><td><b>STUDENTS</b></td></tr>";
        for (Allocation Course : Courses) {
            temp += Course.dply();
        }

        temp = "<html><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No. Of Students Present For Course Allocation &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + StudentDetails.length + "</b><br><table border=\"2\">" + temp + "</table></html>";

        jEditorPane5.setText(temp);
    }

    void insert(String studentid, int teachchoice) throws Exception {
        if (teachchoice == 1) {
            RollList1.insertlast(Teacher1, studentid);
        } else if (Teacher2.equals("  ")) {
            System.out.println("Wrong Choice");
        } else if (teachchoice == 2 && !(Teacher2.equals("  "))) {
            RollList2.insertlast(Teacher2, studentid);
        }
    }          //Insert student ID into the Teachers LinkedList

    void PrintAfterDeletion() throws Exception {
        String q = "<tr><td><b>TEACHERS</b></td><td><b>STUDENTS</b></td></tr>";
        Courses[0].DeleteStudents(Courses);
        for (Allocation Course : Courses) {
            q += Course.dply();
        }
        q = "<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>No. Of Students Present For Course Allocation &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + StudentDetails.length + "</b><br><table border=\"2\">" + q + "</table></html>";
        System.out.println(temp);
        jEditorPane5.setText(q);
    }       //Print the students alloted courses after deletion is performed

    void DeleteStudents(Allocation clobj1[]) throws Exception {
        BufferedReader deleteobj = new BufferedReader(new FileReader("delete.txt"));
        String temp, roll, coursedel;
        while ((temp = deleteobj.readLine()) != null) {
            StringTokenizer ssdel = new StringTokenizer(temp);

            roll = ssdel.nextToken();
            coursedel = ssdel.nextToken();

            for (Allocation clobj11 : clobj1) {
                if (coursedel.equals(clobj11.CouseId)) {
                    clobj11.RollList1.delany(clobj11.Teacher1, roll);
                    if (!(clobj11.Teacher2).equals("  ")) {
                        clobj11.RollList2.delany(clobj11.Teacher2, roll);
                    }
                }
            }
        }

    }           //Read delete file and remove student from the teacher linked list

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane2 = new javax.swing.JEditorPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jEditorPane3 = new javax.swing.JEditorPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jEditorPane4 = new javax.swing.JEditorPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jEditorPane5 = new javax.swing.JEditorPane();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FFCS ALLOTMENT CENTRE");

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jEditorPane1.setEditable(false);
        jEditorPane1.setContentType("text/html"); // NOI18N
        jEditorPane1.setToolTipText("Courses offered in the semester");
        jScrollPane2.setViewportView(jEditorPane1);

        jTabbedPane1.addTab("Courses Offered", jScrollPane2);

        jEditorPane2.setEditable(false);
        jEditorPane2.setContentType("text/html"); // NOI18N
        jEditorPane2.setToolTipText("Student-wise academic history");
        jScrollPane1.setViewportView(jEditorPane2);

        jTabbedPane1.addTab("Student History", jScrollPane1);

        jEditorPane3.setEditable(false);
        jEditorPane3.setContentType("text/html"); // NOI18N
        jEditorPane3.setToolTipText("Courses opted by student for the semester");
        jScrollPane3.setViewportView(jEditorPane3);

        jTabbedPane1.addTab("Courses Opted", jScrollPane3);

        jEditorPane4.setEditable(false);
        jEditorPane4.setContentType("text/html"); // NOI18N
        jEditorPane4.setToolTipText("Teachers taking the courses.");
        jScrollPane4.setViewportView(jEditorPane4);

        jTabbedPane1.addTab("Teachers ", jScrollPane4);

        jEditorPane5.setEditable(false);
        jEditorPane5.setContentType("text/html"); // NOI18N
        jScrollPane5.setViewportView(jEditorPane5);

        jTabbedPane1.addTab("Allotment", jScrollPane5);

        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);
        jToolBar1.setOpaque(false);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Refresh.png"))); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Apply.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Delete.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);
        jButton3.getAccessibleContext().setAccessibleName("DeleteButton");

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Button-Refresh-icon.png"))); // NOI18N
        jMenuItem1.setText("Refresh");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Apply.png"))); // NOI18N
        jMenuItem2.setText("Allot Students");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Exit.png"))); // NOI18N
        jMenuItem3.setText("Exit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Help.png"))); // NOI18N
        jMenuItem4.setText("Help");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            callmethods();                          //Refresh
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            callmethods();      //Refresh from menu
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            //Allotment button
            if (AllotmentFlag != 1) {               //Allow Allotment only once
                AllotmentFlag = 1;
                alloted();

            } else {
                dialog("Allotment has already been done!");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            if (AllotmentFlag != 1) {
                AllotmentFlag = 1;              //Allotment from menu
                //allow allotment only once
                alloted();
            } else {
                dialog("Allotment has already been done!");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {

            if (AllotmentFlag != 1) {               //Deallot students. check if allotmnet is done first and the deleition is performed only once
                dialog("Allotment not yet performed");
            } else if (DeleteFlag != 1 && AllotmentFlag == 1) {
                DeleteFlag = 1;
                PrintAfterDeletion();

            } else if (AllotmentFlag == 1 && DeleteFlag == 1) {
                dialog("Deallotment has already been done!");
            }

        } catch (Exception ex) {
            Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed

        JDialog jd;
        jd = new JDialog();   //HELP window
        jd.setTitle("HELP");
        jd.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        jd.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jd.setMinimumSize(new Dimension(500, 650));
        JEditorPane jt1 = new JEditorPane();
        jd.add(jt1);
        jt1.setContentType("text/html");
        jt1.setText("<html><head ><h2 align=\"center\" color=\"red\">HELP</h2></head><br><body><p>Welcome! This is the help for the FFCS Allotment Centre. This software displays the: "
                + "<ul><li> Courses available</li><li> Teachers taking the courses.</li>"
                + "<li>Students History.</li>"
                + "<li>Courses opted by the students for the semester.</li>"
                + "<li>Allot/Deallot the students according to their choices.</li>"
                + "<li>Dislpay the alloted list of students.</li></ul> </p>"
                + "<p>This software uses a file reading system.The files must be wriitem in the correct order and sequence for the software to work.Given below are the formats and names of each file:"
                + "<ul><li><b> teacherinputfile.txt </b>     CourseID_PrerequisiteID_Teacher1_Teacher2</li>"
                + "<li><b>courseopted1.txt</b>     ID_Name_Year_(CoursesTeacherchosen_)xN</li>"
                + "<li><b>history.txt</b>     Name_ID_Year_((CoursesDoneGrades)/-)xN</li>"
                + "<li><b>delete.txt</b>     ID deletecourseID</li><ul></p>"
                + "<p><h3>Note:<h3><ul><li>Each Teacher has a classroom capacity of 6.</li><li>Any number of students can be added in the student history and coursesopted1 file. </li>"
                + "<li>Teacher can be chosen by using either 1 or 2.</li>"
                + "<li>Non conformity to structure of files will lead to error or unalloted students.</li></ul></p></body></html>");
        jt1.setEditable(false);
        jt1.setEnabled(false);
        jt1.setDisabledTextColor(Color.black);
        jt1.setVisible(true);
        jd.setVisible(true);
       
        
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    void dialog(String a) {
        JDialog jd1;
        jd1 = new JDialog();
        jd1.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        jd1.setTitle("Warning!");
        jd1.setMinimumSize(new Dimension(300, 100));
        JTextField jt3 = new JTextField();
        jd1.add(jt3);
        jd1.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jt3.setHorizontalAlignment(JTextField.CENTER);
        jt3.setEditable(false);
        jt3.setDisabledTextColor(Color.black);
        jt3.setOpaque(false);
        jt3.setText(a);
        jt3.setEnabled(false);
        jt3.setDisabledTextColor(Color.black);
        jd1.setResizable(false);
        jt3.setVisible(true);
        jd1.setVisible(true);
    }              // Display appropriate messages 

    String dply() {
        String r = "", e = "";
        r = RollList1.display(Teacher1);
        r = "<tr>" + r + "</tr>";
        if (!(Teacher2.data.equals("  "))) {
            e += RollList2.display(Teacher2);
            e = "<tr>" + e + "</tr>";
        }
        r = r + e;
        return (r);
    }          // Display the list of students under each teacher

    public static void main(String args[]) throws FileNotFoundException, IOException, Exception {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Allocation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Allocation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Allocation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Allocation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Allocation().setVisible(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JEditorPane jEditorPane2;
    private javax.swing.JEditorPane jEditorPane3;
    private javax.swing.JEditorPane jEditorPane4;
    private javax.swing.JEditorPane jEditorPane5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
