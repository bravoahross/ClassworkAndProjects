package com.example.as1;

/**
 * Interface used by Station RecycleViews in order to give them a click function
 * @author Alex Brown
 */
public interface RecyclerViewInterface {
    /**
     * Given the position of the item selected in the recycle view, pull that item from the list and do the propper intent and start activity.
     * @param position
     */
    void onItemClick(int position);
}
