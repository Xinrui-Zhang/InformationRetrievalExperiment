/**
 * @创建人 柳靖洋
 * @创建时间 2021/5/10
 * @描述
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import org.json.*;
import java.io.*;
import java.text.CollationKey;
import java.text.Collator;

public class UIclass extends JFrame{

    public JTextField searchText;
    public JButton search;
    public JSlider jSlider;
    public static JTextArea showArea;
    public static JScrollPane scroll;
    public ArrayList<Character> text;
    private searchMethod sm;
    public TreeMap<Character,Term> getDictionary;
    public dataProcessor data;
    private final TreeMap<Character,Term> terms;
    private final TreeMap<String, Poet> poets;
    private Color backColor;
    public UIclass() throws IOException {
        super();
        this.setSize(1200, 800);     //窗口大小
        this.getContentPane().setLayout(null);
        this.add(getJTextField(), null);
        this.add(getJButton(), null);
        this.add(getScrollPane(),null);
        //this.add(getJTextArea(),null);
        this.setTitle("信息检索");

        this.getContentPane().setBackground(new Color(248,232,193));
        String path="IRproject/dataset/poet.tang.1000.json";
        //String path =".\\dataset\\poet.tang.1000.json";
        dataProcessor.readFile(path);
        poets = dataProcessor.getPoets();
        terms = dataProcessor.getPoetTerm();

    }



    private JTextField getJTextField()
    {
        if(searchText == null) {
            searchText = new javax.swing.JTextField();
            searchText.setBounds(300, 50, 300, 20);
        }
        return searchText;
    }

    private JButton getJButton()
    {
        if(search == null) {
            search = new javax.swing.JButton();
            search.setBounds(820, 50, 80, 20);
            search.setText("搜索");
        }
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showArea.setText("");
                searchMethod s = new searchMethod();
                String text;
                text=searchText.getText();
                TreeMap<String, Integer> result = s.query(text,terms);
                for(String d : result.keySet()) {
                    Poet t = poets.get(d);
                    showArea.append(t.getTitle()+"\n"+t.getAuthor()+"\n"+t.getParagraphs()+"\n");
                }
            }
        });
        return search;

    }
    private JTextArea getJTextArea()
    {
        if(showArea == null)
        {
            showArea =new JTextArea();
            showArea.setBounds(300,100,380,500);

        }
        return showArea;
    }
    private JScrollPane getScrollPane()
    {
        if(scroll == null)
        {
            scroll =new JScrollPane();
            scroll.setBounds(300,100,600,500);
            scroll.setViewportView(getJTextArea());
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
        return scroll;
    }
    public static void main(String[] args) throws IOException {

         UIclass w = new UIclass();
         w.setVisible(true);                         //设为可见






     }


}
