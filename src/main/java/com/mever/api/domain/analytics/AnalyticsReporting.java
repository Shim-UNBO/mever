package com.mever.api.domain.analytics;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;

import com.google.api.services.analyticsreporting.v4.model.*;
import com.mever.api.domain.analytics.dto.AnalyticsDto;


public class AnalyticsReporting {
    private static final String APPLICATION_NAME = "Hello Analytics Reporting";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String KEY_FILE_LOCATION = "C:\\Users\\PC\\IdeaProjects\\mever\\src\\main\\java\\com\\mever\\api\\domain\\analytics\\client_secrets.json";
    private static final String VIEW_ID = "268763310";

    public static List<AnalyticsDto> analyticReport() {
        List<AnalyticsDto> analyticsList = null;
        try {
            com.google.api.services.analyticsreporting.v4.AnalyticsReporting service = initializeAnalyticsReporting();
            GetReportsResponse response = getReport(service);
            analyticsList = printResponse(response);
//            printResponse(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        for (int i=0;i<analyticsList.size();i++){
//            System.out.println("test : "+analyticsList.get(i));
//        }
        return analyticsList;
    }

    /**
     * Initializes an Analytics Reporting API V4 service object.
     *
     * @return An authorized Analytics Reporting API V4 service object.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private static com.google.api.services.analyticsreporting.v4.AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = GoogleCredential
                .fromStream(new FileInputStream(KEY_FILE_LOCATION))
                .createScoped(AnalyticsReportingScopes.all());

        // Construct the Analytics Reporting service object.
        return new com.google.api.services.analyticsreporting.v4.AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Queries the Analytics Reporting API V4.
     *
     * @param service An authorized Analytics Reporting API V4 service object.
     * @return GetReportResponse The Analytics Reporting API V4 response.
     * @throws IOException
     */
    private static GetReportsResponse getReport(com.google.api.services.analyticsreporting.v4.AnalyticsReporting service) throws IOException {
        // Create the DateRange object.
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("7DaysAgo");
        dateRange.setEndDate("today");

        // Metrics(조회할 컬럼) 객체 생성
        Metric sessions = new Metric()
                .setExpression("ga:sessions")
                .setAlias("sessions");

        Metric newUsers = new Metric().setExpression("ga:newUsers")
                .setAlias("newUsers");

        Metric users = new Metric().setExpression("ga:users")
                .setAlias("users");

        Metric sessionDuration = new Metric().setExpression("ga:avgSessionDuration")
                .setAlias("sessionDuration");

        Metric pageviews = new Metric().setExpression("ga:pageviews")
                .setAlias("pageviews");

        Metric hits = new Metric().setExpression("ga:hits")
                .setAlias("hits");

        Metric percentNewSessions = new Metric().setExpression("ga:percentNewSessions")
                .setAlias("percentNewSessions");


        // 여러 Metrics를 사용할 경우
        List<Metric> MetricList = new ArrayList<>();
        MetricList.add(pageviews);
        MetricList.add(newUsers);
        MetricList.add(users);
        MetricList.add(sessionDuration);
        MetricList.add(hits);
        MetricList.add(percentNewSessions);

        //Dimension별 조사가 이뤄진다. ex) 페이지타이틀별,성별별,브라우저별
        Dimension pageTitle = new Dimension().setName("ga:pageTitle");
        Dimension browser = new Dimension().setName("ga:browser");

        List<OrderBy> orderBys = new ArrayList<>();
        OrderBy orderBy = new OrderBy().setFieldName("pageviews")
                .setSortOrder("ascending");
        orderBys.add(orderBy);

//        List<DateRangeValues> metrics = row.getMetrics();
//        for (DateRangeValues dateRangeValues : metrics) {
//            List<String> values = dateRangeValues.getValues();
//            for (int i = 0; i < MetricList.size(); i++) {
//                Metric metric = MetricList.get(i);
//                if (metric.getExpression().equals("ga:sessionDuration")) {
//                    double sessionDurationInSeconds = Double.parseDouble(values.get(i));
//                    int minutes = (int) (sessionDurationInSeconds / 60);
//                    int seconds = (int) (sessionDurationInSeconds % 60);
//                    System.out.println("평균 페이지 머문 시간: " + minutes + "분 " + seconds + "초");
//                }
//            }

        // Create the ReportRequest object.
        ReportRequest request = new ReportRequest()
                .setViewId(VIEW_ID)
                .setDateRanges(Arrays.asList(dateRange))
                .setMetrics(Arrays.asList(sessions))
                .setMetrics(MetricList)
                .setDimensions(Arrays.asList(pageTitle))
//                .setDimensions(Arrays.asList(browser))
                .setOrderBys(orderBys);


        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = service.reports().batchGet(getReport).execute();

        // Return the response.
        return response;
    }

    /**
     * Parses and prints the Analytics Reporting API V4 response.
     *
     * @param response An Analytics Reporting API V4 response.
     */
    private static List<AnalyticsDto> printResponse(GetReportsResponse response) {
        List<AnalyticsDto> analyticsList = new ArrayList<>();

        for (Report report: response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                System.out.println("No data found for " + VIEW_ID);
                return analyticsList;
            }

            for (ReportRow row: rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();
                AnalyticsDto analyticsDto = new AnalyticsDto();

                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    String dimensionHeader = dimensionHeaders.get(i);
                    String dimensionValue = dimensions.get(i);
                    System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                    if (dimensionHeader.equals("dimension")) {
                        analyticsDto.setDimension(dimensionValue);
                    } else if (dimensionHeader.equals("ga:pageTitle")) {
                        analyticsDto.setPage_title(dimensionValue);
                    }
                }
                for (int j = 0; j < metrics.size(); j++) {
                    System.out.print("Date Range (" + j + "): ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        MetricHeaderEntry metricHeaderEntry = metricHeaders.get(k);
                        String metricName = metricHeaderEntry.getName();
                        String metricValue = values.getValues().get(k);
                        if (metricName.equals("pageviews")) {
                            analyticsDto.setPage_views(metricValue);
                        } else if (metricName.equals("users")) {
                            analyticsDto.setUsers(metricValue);
                        } else if (metricName.equals("sessions")) {
                            analyticsDto.setSessions(metricValue);
                        } else if (metricName.equals("newUsers")) {
                            analyticsDto.setNewUsers(metricValue);
                        }else if (metricName.equals("percentNewSessions")) {
                            analyticsDto.setPercentNewSessions(metricValue);
                        }
                        if (metricName.equals("sessionDuration")) {
                            double durationInSeconds = Double.parseDouble(metricValue);
                            int minutes = (int) (durationInSeconds / 60);
                            int seconds = (int) (durationInSeconds % 60);
                            String avgSession = minutes+"분"+seconds+"초";
                            analyticsDto.setAvgSessions(avgSession);
                            System.out.println("평균 페이지 머문 시간: " + minutes + "분 " + seconds + "초");
                        } else {
                            System.out.println(metricName + ": " + metricValue);
                        }
                    }
                    LocalDate currentDate = LocalDate.now();
                    LocalDate sevenDaysAgo = currentDate.minusDays(7);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String currentDateString = currentDate.format(formatter);
                    String sevenDaysAgoString = sevenDaysAgo.format(formatter);
                    analyticsDto.setStart_ymd(sevenDaysAgoString);
                    analyticsDto.setEnd_ymd(currentDateString);
                    analyticsList.add(analyticsDto);
                }
            }
        }
        return  analyticsList;
    }

}