package searchengine.parsers;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.dto.statistics.PageData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;


@Slf4j
public class PageUrlParser extends RecursiveTask<List<PageData>> {

    private final String url;
    private final List<String> urlList;
    private final List<PageData> pageDataList;

    String userAgent = "Mozilla/5.0 (X11; Fedora;Linux x86; rv:60.0) Gecko/20100101 Firefox/60.0";
    String referer = "https://www.google.com";

    public PageUrlParser(String url, List<String> urlList, List<PageData> pageDataList) {
        this.url = url;
        this.urlList = urlList;
        this.pageDataList = pageDataList;
    }

    @Override
    protected List<PageData> compute() {
        try {
            Thread.sleep(300);
            Document doc = getConnect(url);
            String html = doc.outerHtml();
            Connection.Response response = doc.connection().response();
            int status = response.statusCode();
            PageData pageData = new PageData(url, html, status);
            pageDataList.add(pageData);
            Elements elements = doc.select("body").select("a");
            List<PageUrlParser> taskList = new ArrayList<>();
            for (Element e : elements) {
                String link = e.attr("abs:href");
                if (link.startsWith(e.baseUri()) && !link.equals(e.baseUri()) && !link.contains("#") &&
                        !link.contains(".pdf") && !link.contains(".jpg") && !link.contains(".JPG") &&
                        !link.contains(".png") && !urlList.contains(link)) {

                    urlList.add(link);
                    PageUrlParser task = new PageUrlParser(link, urlList, pageDataList);
                    task.fork();
                    taskList.add(task);
                }
            }

            taskList.forEach(ForkJoinTask::join);


        } catch (Exception ex) {
            log.debug("Ошибка парсинга " + url);
            PageData pageData = new PageData(url, "", 500);
            pageDataList.add(pageData);

        }
        return pageDataList;
    }


    private Document getConnect(String url) {
        Document doc = null;
        try {
            Thread.sleep(300);
            doc = Jsoup.connect(url).userAgent(userAgent).referrer(referer).get();

        } catch (Exception ex) {
            log.debug("Соединение с " + url + " не установленно");
        }
        return doc;
    }

}
