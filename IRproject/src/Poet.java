/**
 * @author 张心睿
 * @date 2021/5/6
 * @description
 */
public class Poet {
    private String title;
    private String author;
    private String paragraphs;
    private String pid;
    /**
     * @author 张心睿
     * @description
     * @date 19:38 2021/5/6
     * @param t, a, p, i
     * @return
     **/
    Poet(String t, String a, String p, String i){
        setTitle(t);
        setAuthor(a);
        setParagraphs(p);
        setPid(i);
    }
    /**
     * @author 张心睿
     * @description 获取不包含标点符号的诗词内容
     * @date 19:40 2021/5/6
     * @param
     * @return java.lang.String
     **/
    public String getContent(){
        return paragraphs.replaceAll("\\pP","");
    }

    /**
     * @author 张心睿
     * @description 打印单首诗词的标题、作者、内容
     * @date 19:40 2021/5/6
     * @param
     * @return void
     **/
    public void print(){
        System.out.println(this.getTitle());
        System.out.println(this.getAuthor());
        System.out.println(this.getParagraphs());
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:38 2021/5/6
     * @param title
     * @return void
     **/
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:39 2021/5/6
     * @param author
     * @return void
     **/
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:39 2021/5/6
     * @param paragraphs
     * @return void
     **/
    public void setParagraphs(String paragraphs) {
        this.paragraphs = paragraphs;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:39 2021/5/6
     * @param pid
     * @return void
     **/
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:39 2021/5/6
     * @param
     * @return java.lang.String
     **/
    public String getTitle() {
        return title;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:39 2021/5/6
     * @param
     * @return java.lang.String
     **/
    public String getAuthor() {
        return author;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:39 2021/5/6
     * @param
     * @return java.lang.String
     **/
    public String getParagraphs() {
        return paragraphs;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:39 2021/5/6
     * @param
     * @return java.lang.String
     **/
    public String getPid() {
        return pid;
    }

}
