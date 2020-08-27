package rockets.mining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rockets.dataaccess.DAO;
import rockets.model.Launch;
import rockets.model.LaunchServiceProvider;
import rockets.model.Rocket;
import rockets.model.Launch.LaunchOutcome;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RocketMiner {
    private static Logger logger = LoggerFactory.getLogger(RocketMiner.class);

    private DAO dao;

    public RocketMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * TODO: to be implemented & tested!
     * Returns the top-k active rocket, as measured by number of launches.
     *
     * @param k the number of rockets to be returned.
     * @return the list of k most active rockets.
     */
    public List<Rocket> mostLaunchedRockets(int k) {
    	logger.info("find most " + k + " launched rockets");

        Map<Rocket,Integer> rocketLaunched=eachRocketLaunchednum();
        Comparator<Map.Entry<Rocket,Integer>> rocketLaunchNumComparator = (a, b) -> a.getValue().compareTo(b.getValue());
        List<Map.Entry<Rocket,Integer>> list = new ArrayList<Map.Entry<Rocket,Integer>>(rocketLaunched.entrySet());
        Collections.sort(list, rocketLaunchNumComparator);
        List<Rocket> result=new ArrayList<Rocket>();
        list.stream().sorted(rocketLaunchNumComparator).limit(k).forEach(l->{
            result.add(l.getKey());
        });
        return result;
     }
  //Return a map, which list each rocket launched amount 
    public Map<Rocket,Integer> eachRocketLaunchednum(){
        Collection<Launch> lauchedcollection=dao.loadAll(Launch.class);
        Map<Rocket,Integer> rocketLaunched=new HashMap<Rocket,Integer>();
        lauchedcollection.forEach(launch->{
            if(rocketLaunched.containsKey(launch.getLaunchVehicle())) {
                rocketLaunched.put(launch.getLaunchVehicle(),rocketLaunched.get(launch.getLaunchVehicle())+1);
            }
            else {rocketLaunched.put(launch.getLaunchVehicle(),1);}
        });
        return rocketLaunched;
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most reliable launch service providers as measured
     * by percentage of successful launches.
     *
     * @param k the number of launch service providers to be returned.
     * @return the list of k most reliable ones.
     */
    public List<LaunchServiceProvider> mostReliableLaunchServiceProviders(int k) {
        
        Collection<LaunchServiceProvider> lsps = dao.loadAll(LaunchServiceProvider.class);
        Collection<Launch> launches = dao.loadAll(Launch.class);
        List<Launch> launchList = new ArrayList<>(launches);
        List<LaunchServiceProvider> lspList = new ArrayList<>(lsps);
        Map<LaunchServiceProvider, Double> lspMap = new LinkedHashMap<>();
        double successfulLaunches=0.0;
        double allLaunches=0.0;
        // double failedLaunches=0;
        for(LaunchServiceProvider launchServiceProvider : lspList)
        {
            for(Launch launch : launchList)
            {
                if(launch.getLaunchOutcome().equals(Launch.LaunchOutcome.SUCCESSFUL))
                {
                    successfulLaunches++;
                }
                allLaunches++;
            }
            if(allLaunches!=0) {
                double ratio = successfulLaunches / allLaunches;
                lspMap.put(launchServiceProvider, ratio);
            }

        }
        Map<LaunchServiceProvider,Double> sortedLsp = lspMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(k)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));
        List<LaunchServiceProvider> topLSP = new ArrayList<>();
        for (LaunchServiceProvider lsp : sortedLsp.keySet()){
            topLSP.add(lsp);
        }
        return topLSP;
    }


    /**
     * <p>
     * Returns the top-k most recent launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most recent launches.
     */
    public List<Launch> mostRecentLaunches(int k) {
       
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Comparator<Launch> launchDateComparator = (a, b) -> -a.getLaunchDate().compareTo(b.getLaunchDate());
        return launches.stream().sorted(launchDateComparator).limit(k).collect(Collectors.toList());
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the successful launch rate in <code>year</code> measured by the
     * number of successful launches and total number of launches
     *
     * @param year the year
     * @return the successful launch rate in BigDecimal with scale 2.
     */
    public BigDecimal successfulLaunchRateInYear(int year) {
    	Collection<Launch> launches = dao.loadAll(Launch.class);
        Predicate<Launch> filterYear = l -> l.getLaunchDate().getYear() == year;
        long successfulCount = launches.stream()
                .filter(filterYear)
                .filter(l -> l.getLaunchOutcome() == Launch.LaunchOutcome.SUCCESSFUL)
                .count();
        long totalCount = launches.stream().filter(filterYear).count();
        return BigDecimal.valueOf(successfulCount/totalCount);
    }

    /**
     * TODO: to be implemented & tested!
     * <p>
     * Returns the top-k most expensive launches.
     *
     * @param k the number of launches to be returned.
     * @return the list of k most expensive launches.
     */
    public List<Launch> mostExpensiveLaunches(int k) {
    	logger.info("find most expensive " + k + " launches");
        Collection<Launch> launches = dao.loadAll(Launch.class);
        Comparator<Launch> launchPriceComparator = (a, b) -> - a.getPrice() + (b.getPrice());
        return launches.stream().sorted(launchPriceComparator).limit(k).collect(Collectors.toList());
    }
}
