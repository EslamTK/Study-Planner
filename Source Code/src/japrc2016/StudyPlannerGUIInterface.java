package japrc2016;

public interface StudyPlannerGUIInterface
{
    /** 
     * Tells the GUI that something about the simulation has changed (and hence the GUI may need to redraw itself) 
     * 
     */
    void notifyModelHasChanged();
}
