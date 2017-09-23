import java.util.*;
import edu.duke.*;

public class EarthQuakeClient {
    public EarthQuakeClient() {
        // TODO Auto-generated constructor stub
    }

    private ArrayList<QuakeEntry> filterByMagnitude(ArrayList<QuakeEntry> quakeData,
    double magMin) {
        ArrayList<QuakeEntry> answer = new ArrayList<QuakeEntry>();
        for (QuakeEntry qe : quakeData)  {
            if (qe.getMagnitude() > magMin) {
                answer.add(qe);
            }
        }
        return answer;
    }

    private ArrayList<QuakeEntry> filterByDistanceFrom(  ArrayList<QuakeEntry> quakeData,
                                                        double distMax,
                                                        Location from   ) {
        ArrayList<QuakeEntry> answer = new ArrayList<QuakeEntry>();

        for (QuakeEntry qe : quakeData)  {
            Location to = qe.getLocation();
            double dist = from.distanceTo(to);
            double distMaxMeters = distMax*1000;
            if (dist < distMaxMeters) {
                answer.add(qe);
            }
        }
        return answer;
    }
    private ArrayList<QuakeEntry> filterByDepth( ArrayList<QuakeEntry> quakeData, 
                                                double minDepth,
                                                double maxDepth ) {
        ArrayList<QuakeEntry> answer = new ArrayList<QuakeEntry>();
        for (QuakeEntry qe : quakeData)  {
            double depth = qe.getDepth();
            if ( depth > minDepth && depth < maxDepth ) {
                answer.add(qe);
            }
        }
        return answer;
    }
    private boolean wherePhrase(String s, String where, String phrase){
        switch (where){
                case "start":
                    if( s.startsWith(phrase) ) return true;
                    break;
                case "end":
                    if( s.endsWith(phrase) ) return true;
                    break;
                case "any":
                    if( s.indexOf(phrase) != -1 ) return true;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid where argument: " + where);
            }
        return false;
    }
    private ArrayList<QuakeEntry> filterByPhrase(   ArrayList<QuakeEntry> quakeData, 
                                                    String where,
                                                    String phrase ) {
        ArrayList<QuakeEntry> answer = new ArrayList<QuakeEntry>();
        for (QuakeEntry qe : quakeData)  {
            String title = qe.getInfo();
            if(wherePhrase(title,where,phrase)) answer.add(qe);
        }
        return answer;
    }
    
    public void dumpCSV(ArrayList<QuakeEntry> list){
        System.out.println("Latitude,Longitude,Magnitude,Info");
        for(QuakeEntry qe : list){
            System.out.printf("(%4.2f,%4.2f), mag = %4.2f, depth = %4.2f, title = %s\n",
                                qe.getLocation().getLatitude(),
                                qe.getLocation().getLongitude(),
                                qe.getMagnitude(),
                                qe.getDepth(),
                                qe.getInfo());
        }

    }
    public void bigQuakes() {
        EarthQuakeParser parser = new EarthQuakeParser();
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        String source = "data/nov20quakedata.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        System.out.println("read data for "+list.size()+" quakes");
        
        ArrayList<QuakeEntry> listBig = this.filterByMagnitude(list, 5.0);
        
        for(QuakeEntry qe : listBig){
            System.out.println(qe);
        }

    }

    public void closeToMe(){
        EarthQuakeParser parser = new EarthQuakeParser();
        String source = "data/nov20quakedatasmall.atom"; //"http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        System.out.println("read data for "+list.size()+" quakes");

        // This location is Durham, NC
        //Location city = new Location(35.988, -78.907);
        

        // This location is Bridgeport, CA
         Location city =  new Location(38.17, -118.82);
        ArrayList<QuakeEntry> listClose = this.filterByDistanceFrom(list, 1000, city);
        for(QuakeEntry qe : listClose){
            Location to = qe.getLocation();
            double distInMeters = city.distanceTo(to)/1000.0;
            System.out.println( distInMeters + " " + qe.getInfo());
        }
        System.out.println("Found " +listClose.size() +" quakes that match that criteria");
    }
    public void quakesOfDepth(){
        EarthQuakeParser parser = new EarthQuakeParser();
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        String source = "data/nov20quakedata.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        System.out.println("read data for "+list.size()+" quakes");
        ArrayList<QuakeEntry> listDepth = this.filterByDepth(list, -10000.0, -8000.0);
        this.dumpCSV(listDepth);
        
        System.out.println("Found "+listDepth.size()+" quakes that match that criteria");
    }
    public void quakesByPhrase(){
        EarthQuakeParser parser = new EarthQuakeParser();
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        String source = "data/nov20quakedata.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        System.out.println("read data for "+list.size()+" quakes");
        String phrase = "Creek";
        String where = "any";
        ArrayList<QuakeEntry> listPhrase = this.filterByPhrase(list, where, phrase);
        this.dumpCSV(listPhrase);
        
        System.out.println("Found "+listPhrase.size()+" quakes that match " + phrase + " at " + where);
    }
    public void createCSV(){
        EarthQuakeParser parser = new EarthQuakeParser();
        String source = "data/nov20quakedatasmall.atom";
        //String source = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.atom";
        ArrayList<QuakeEntry> list  = parser.read(source);
        dumpCSV(list);
        System.out.println("# quakes read: " + list.size());
        for (QuakeEntry qe : list) {
            System.out.println(qe);
        }
    }
    
}
