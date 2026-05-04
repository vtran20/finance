package com.tdameritrade;

import com.easysoft.finance.browser.LoginPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdameritrade.stock.StockInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TDAmeritradeUtil {



    public enum  Strategy {
        COVERED("COVERED"), VERTICAL("VERTICAL"), CALENDAR("CALENDAR"), STRANGLE("STRANGLE"), STRADDLE("STRADDLE"), BUTTERFLY("BUTTERFLY"),
        CONDOR("CONDOR"), IRON_CONDOR("IRON_CONDOR"), DIAGONAL("DIAGONAL"), COLLAR("COLLAR"), ROLL("ROLL");

        private String strategyType;

        Strategy(String strategyType) {
            this.strategyType = strategyType;
        }

        @Override
        public String toString() {
            return this.strategyType;
        }
    }

    public enum OptionType {
        CALL ("CALL"), PUT("PUT"), ALL ("ALL");

        private String optionType;

        OptionType(String optionType) {
            this.optionType = optionType;
        }
        @Override
        public String toString() {
            return this.optionType;
        }
    }


    public static String TD_AMERITRADE_URL = "https://api.tdameritrade.com/v1";
    public static String TD_AMERITRADE_API_KEY [] = {"WKSETQUMDUZQGLHDYZHGRIG1L7JJ1WCB"};
    public static String TD_AMERITRADE_API_TOKEN_PATH = "token";
    public static String TD_AMERITRADE_API_ACCESS_TOKEN = "";

    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssz";

    public static Integer totalRequestCountUsed = 0;
    public static Integer MAX_REQUEST_MIN = 100;


    private static Map <String, Integer> apiKeyMap = Collections.synchronizedMap(new HashMap<String, Integer>());
    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH:mm");


    /**
     * This method return Delta for a specific symbol option
     *
     * @param optionSymbol
     * @param optionType
     * @return
     *
     * @Deprecated Using Symbols api to get delta. It is faster and simpler.
     */
    public static double getDELTAOption (String optionSymbol,  String optionType) {

        double delta = 0;

        String symbol = optionSymbol.substring(0, optionSymbol.indexOf("_"));
        Map response = getOptionData(symbol, null, optionType, "10");

        if (OptionType.CALL.toString().equals(optionType)) {
            Map callExpDateMap = getCallOptionData(response);
            delta = getDELTAOption(callExpDateMap, optionSymbol);
        } else if (OptionType.PUT.toString().equals(optionType)) {
            Map putExpDateMap = getPutOptionData(response);
            delta = getDELTAOption(putExpDateMap, optionSymbol);
        } else {
            Map callExpDateMap = getCallOptionData(response);
            delta = getDELTAOption(callExpDateMap, optionSymbol);
            if (delta == 0) {
                Map putExpDateMap = getPutOptionData(response);
                delta = getDELTAOption(putExpDateMap, optionSymbol);
            }
        }
        return delta;
    }
    /**
     * This method return Delta for a specific symbol option
     *
     * @param expDateMap
     * @param optionSymbol
     * @return
     *
     * @Deprecated Using Symbols api to get delta. It is faster and simpler.
     */
    private static double getDELTAOption (Map expDateMap, String optionSymbol) {
        if (expDateMap != null) {
            for (Object key : expDateMap.keySet()) {
                for (Object priceKey : ((Map) expDateMap.get(key)).keySet()) {
                    List callList = (List) ((Map) expDateMap.get(key)).get(priceKey);
                    if (callList != null && callList.size() > 0) {
                        Map callOption = (Map) callList.get(0);
                        if (optionSymbol.equals(callOption.get("symbol"))) {
                            return (double) callOption.get("delta");
                        }
                    }
                }
            }
        }
        return 0;
    }
    private static Map getCallOptionData (Map optionData) {
        return (Map) optionData.get("callExpDateMap");
    }
    private static Map getPutOptionData (Map optionData) {
        return (Map) optionData.get("putExpDateMap");
    }
    /**
     * Getting Option Data
     *
     * @param symbol
     * @param toDate
     *
     * @return
     */
    public static Map getOptionData (String symbol, String toDate, String optionType, String strikeCount ) {

        Map result = new LinkedHashMap();

        //Get next 10 days if toDate is empty
        if (StringUtils.isEmpty(toDate)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 10);
            toDate = DateFormatUtils.format(calendar.getTime(), DATE_FORMAT);
        }
        if (StringUtils.isEmpty(strikeCount)) {
            strikeCount = "10";
        }

        if (StringUtils.isEmpty(optionType)) {
            optionType = OptionType.ALL.toString();
        }
        RestTemplate restTemplate = new RestTemplate();
        Map response = restTemplate.getForObject(TD_AMERITRADE_URL+ "/marketdata/chains?apikey={apiKey}&symbol={symbol}&strikeCount={strikeCount}&strategy=SINGLE&toDate={toDate}&contractType={contractType}", Map.class, getAPIKey(), symbol, strikeCount, toDate, optionType);

        LinkedHashMap callExpDateMap = (LinkedHashMap) response.get("callExpDateMap");
        if (callExpDateMap != null) {
            for (Object key : callExpDateMap.keySet()) {
                System.out.println(key);
                System.out.println(callExpDateMap.get(key));

            }
        }

        LinkedHashMap putExpDateMap = (LinkedHashMap) response.get("putExpDateMap");
        if (putExpDateMap != null) {
            for (Object key : putExpDateMap.keySet()) {
                System.out.println(key);
                System.out.println(putExpDateMap.get(key));

            }
        }

        return response;
    }

    /**
     * This method will call and get option data. Then calculate to get the expected moving follow the formula
     *
     * ((Sell Call ATM + Sell Put ATM) * 68)/strike price
     *
     * @param symbols
     * @return
     */
    public static Map getExpectedMovingFromStraddleFromSymbols (String symbols) {
        Map <String, Map>map = new LinkedHashMap();
        String [] symbolArr = symbols.split(",");
        Map straddleData = null;
        for (String symbol : symbolArr) {
            straddleData = getExpectedMovingFromStraddleFromSymbol(symbol.strip());
            if (straddleData != null) {
                map.put(symbol.strip(), getExpectedMovingFromStraddleFromSymbol(symbol.strip()));
            }
        }

        List<Map.Entry<String, Map>> entries =
                new ArrayList<Map.Entry<String, Map>>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Map>>() {
            public int compare(Map.Entry<String, Map> a, Map.Entry<String, Map> b){
                return Double.compare(Double.parseDouble(b.getValue().get("result")+""), Double.parseDouble(a.getValue().get("result")+""));
            }
        });

        Map<String, Map> sortedMap = new LinkedHashMap<String, Map>();
        for (Map.Entry<String, Map> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     * This method will call and get option data. Then calculate to get the expected moving follow the formula
     *
     * ((Sell Call ATM + Sell Put ATM) * 68)/strike price
     *
     * @param symbol
     * @return
     */
    public static Map getExpectedMovingFromStraddleFromSymbol (String symbol) {
        Map data = new HashMap();

        double result = 0d;

        RestTemplate restTemplate = new RestTemplate();
        Map response = restTemplate.getForObject(TD_AMERITRADE_URL+ "/marketdata/chains?apikey={apiKey}&symbol={symbol}&strikeCount=4&strategy=STRADDLE&includeQuotes=TRUE", Map.class, getAPIKey(), symbol);

        Map underlying = (Map) response.get("underlying");
        if (underlying == null) {
            System.out.println("This symbol doesn't have date: " + symbol);
            return null;
        }
        double currentAskingPrice = (double) underlying.get("ask");

        List<Map>   monthlyStrategyList = (List<Map>) response.get("monthlyStrategyList");
        if (monthlyStrategyList != null && monthlyStrategyList.size() > 0) {
            Map closestExpired = monthlyStrategyList.get(0);
            List<Map> optionStrategyList = (List<Map>) closestExpired.get("optionStrategyList");
            double strategyStrike;
            double strategyBid;
            for (Map optionStrategy : optionStrategyList) {
                strategyStrike =  Double.parseDouble(optionStrategy.get("strategyStrike")+"");
                if (currentAskingPrice < strategyStrike) {
                    strategyBid = (double) optionStrategy.get("strategyBid");
                    result = (strategyBid * 68)/strategyStrike;
                    break;
                }
            }
        }

        data.put("name", underlying.get("description"));
        data.put("result", result);

        return data;
    }

    public static void openTALoginPage () {

    }

    /**
     * TODO: Authenticate user automatically.
     *
     * @param customerKey
     * @return
     */
    public static Map authenticate (String customerKey) {

        RestTemplate restTemplate = new RestTemplate();
        String newObj = restTemplate.getForObject("https://auth.tdameritrade.com/auth?response_type=code&redirect_uri={callbackURL}&client_id={customerKey}@AMER.OAUTHAP", String.class, "https://localhost/test", getAPIKey());

        ObjectMapper mapper = new ObjectMapper();
        Map<String,StockInfo> map = new HashMap<String,StockInfo>();
        try {
            //convert JSON string to Map
            map = mapper.readValue(newObj, new TypeReference<HashMap<String,StockInfo>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * TODO: This method get the security token and need to finish
     *
     * @param customerKey
     * @return
     */
    public static Map getSecurityToken (String customerKey) {
        RestTemplate restTemplate = new RestTemplate();
        String newObj = restTemplate.getForObject("https://developer.tdameritrade.com/authentication/apis/post/token-0", String.class, "https://localhost", customerKey);

        ObjectMapper mapper = new ObjectMapper();
        Map<String,StockInfo> map = new HashMap<String,StockInfo>();
        try {
            //convert JSON string to Map
            map = mapper.readValue(newObj, new TypeReference<HashMap<String,StockInfo>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Get a Symbol information real time.
     *
     * @param symbol
     * @return
     */
    public static Map getSymbol (String symbol) {
        RestTemplate restTemplate = new RestTemplate();
        String newObj = restTemplate.getForObject(TD_AMERITRADE_URL + "/marketdata/{symbol}/quotes?apikey={apikey}", String.class, symbol, getAPIKey());

        ObjectMapper mapper = new ObjectMapper();
        Map<String,StockInfo> map = new HashMap<String,StockInfo>();
        try {
            //convert JSON string to Map
            map = mapper.readValue(newObj, new TypeReference<HashMap<String,StockInfo>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Return an available key for request. If it doesn't have, waiting for a minutes and try again.
     *
     * Map will store number of time the each key already used for request and based on that we will move to the next available key or wait for a next minute.
     *
     * map = {
     *     key1_time: number of time was used for request,
     *     key2_time: number of time was used for request
     * }
     *
     * @return
     */
    public static String getAPIKey () {
        String key = null;
        Calendar calendar = Calendar.getInstance();
        String dateTime = dateFormat.format(calendar.getTime());
        for (String s : TD_AMERITRADE_API_KEY) {
            String keyMap = s+"_"+dateTime;
            if (apiKeyMap.get(keyMap) != null) {
                Integer count = apiKeyMap.get(keyMap);
                if (count < MAX_REQUEST_MIN) {
                    key = s;
                    apiKeyMap.put(keyMap, count + 1);
                    totalRequestCountUsed++;
                    break;
                } else {
                    //the previous key was full of service, move to the next one
                    continue;
                }
            } else {
                key = s;
                apiKeyMap.put(keyMap, 1);
                totalRequestCountUsed++;
                break;
            }
        }
        if (key == null) {
            printMap();
            try {
                System.out.println("Start sleep 1 minute");
                Thread.sleep(60000);//1 minute
                System.out.println("End sleep 1 minute");
                apiKeyMap.clear();
                return getAPIKey();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Calendar.getInstance().getTime()+"="+key+"_"+dateTime);
        return key;
    }

    public static void printMap() {
        //print map
        for (String stg: apiKeyMap.keySet()) {
            System.out.println(stg +":"+apiKeyMap.get(stg));
        }
        System.out.println("---------");
    }

    public static void main(String[] args) {
        //System.out.println(getDELTAOption("TSLA_030422P815", null));
        LoginPage loginPage = new LoginPage();
        loginPage.Login("","");
//        try {
//            for (int i = 0; i < 31; i++) {
//                System.out.println(TD_AMERITRADE_API_KEY);
//            }
//            printMap();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
