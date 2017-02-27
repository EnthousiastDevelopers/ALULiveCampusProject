package com.tolotranet.livecampus;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Com_MyCustomBaseAdapter extends BaseAdapter {
    private static ArrayList<Com_ItemObject> MyArrayObjects = new ArrayList<Com_ItemObject>();
    private static ArrayList<Com_ItemObject> FilteredObjects = new ArrayList<Com_ItemObject>();
    private Context context;
    private LayoutInflater mInflater;
    private ItemFilter myFilter = new ItemFilter();
    private Integer[] myVoteState;
    private Integer[] myVoteStateOrigin;


    public Com_MyCustomBaseAdapter(Context c, ArrayList<Com_ItemObject> MyList) {
        this.context = c;
        MyArrayObjects = MyList;
        FilteredObjects = MyList;
        myVoteState = new Integer[MyList.size()]; //because votestate will determine the color of the arrows, we need to create one for each answer
        myVoteStateOrigin = new Integer[MyList.size()]; //because votestate will determine the color of the arrows, we need to create one for each answer

        // Arrays.fill(myVoteState, 0); //because it is for debugging, cannot take empty value, since it is used in the switch to determine the color of the arrows, we now fill it with all 0
        for (int i = 0; i < MyList.size(); i++) {
          if(MyList.get(i).getVotes() >1){
              MyList.get(i).setVotes(1);
          }else if (MyList.get(i).getVotes()<-1) {
              MyList.get(i).setVotes(-1);
          }

            myVoteState[i] = MyList.get(i).getVotes(); // because the getvotes tells if the user voted this comment before

            myVoteStateOrigin[i] = MyList.get(i).getVotes(); //because voteState is the switch we need to define wether the user has voted up, voted down or unvoted
        }
        mInflater = LayoutInflater.from(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return FilteredObjects.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return FilteredObjects.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public Filter getFilter() {
        return myFilter;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;

        if (convertView == null) {
            //Log.d("Hello", "In get convertview commycustombaseadapter, just for debugging, position is: "+String.valueOf(position));
            convertView = mInflater.inflate(R.layout.com_list_item, null);
            holder = new ViewHolder();
            holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
            holder.BottomTV = (TextView) convertView.findViewById(R.id.Contact_bottom_tv);
            holder.RightTV = (TextView) convertView.findViewById(R.id.Contact_right_tv);
            holder.downvoteComment = (ImageView) convertView.findViewById(R.id.downvoteComment);
            holder.upvoteComment = (ImageView) convertView.findViewById(R.id.upvoteComment);

            holder.upvoteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(myVoteState.length <= holder.stickyPostion)) {
                    switch (myVoteState[holder.stickyPostion]) {
                        case 1: //because the arrow up is already highlighted
                            holder.upvoteComment.clearColorFilter();
                            holder.downvoteComment.clearColorFilter();
                            myVoteState[holder.stickyPostion] = 0;
                            break;
                        case 0:
                            holder.upvoteComment.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                            holder.downvoteComment.clearColorFilter();
                            myVoteState[holder.stickyPostion] = 1;
                            break;
                        case -1: //because the arrow down is highlighted
                            holder.upvoteComment.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                            holder.downvoteComment.clearColorFilter();
                            myVoteState[holder.stickyPostion] = 1;
                            break;
                    }

                    String newVote = ""; //because we need to calculate the new score and the new vote based on the last previous vote

                    if (myVoteStateOrigin[holder.stickyPostion] == 0) {
                        newVote = String.valueOf(myVoteState[holder.stickyPostion]);
                    } else if (myVoteStateOrigin[holder.stickyPostion] == 1) {
                        newVote = String.valueOf(myVoteState[holder.stickyPostion] - 1);
                    } else if (myVoteStateOrigin[holder.stickyPostion] == -1) {
                        newVote = String.valueOf(myVoteState[holder.stickyPostion] + 1);
                    }
                    Log.d("hello", newVote);
                    String newScore = String.valueOf(Integer.parseInt(newVote) * 25);
                    myVoteStateOrigin[holder.stickyPostion] = myVoteState[holder.stickyPostion]; //because we then initialise the votestateorigin to the last state myvotestate


                    //Log.d("Hello", "Arrow up clicked in comment, position is: "+String.valueOf(holder.stickyPostion)+" and votestate is: "+String.valueOf(myVoteState[holder.stickyPostion]));
                    HttpRequestApp.addVote_Score(
                            Sign_User_Object.Email,
                            FilteredObjects.get(position).getAuthor(),
                            "Voted",
                            FilteredObjects.get(position).getObject(),
                            newScore,
                            newVote,
                            "Normal");
                }else {
                        Toast.makeText(context, "You cannot vote for yourself", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.downvoteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(myVoteState.length <= holder.stickyPostion)) {
                    switch (myVoteState[holder.stickyPostion]) {
                        case 1: //because the arrow up is highlighted
                            holder.downvoteComment.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                            holder.upvoteComment.clearColorFilter();
                            myVoteState[holder.stickyPostion] = -1;
                            break;
                        case 0:
                            holder.downvoteComment.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                            holder.upvoteComment.clearColorFilter();
                            myVoteState[holder.stickyPostion] = -1;
                            break;
                        case -1: //because the arrwo down is already highlighted
                            holder.upvoteComment.clearColorFilter();
                            holder.downvoteComment.clearColorFilter();
                            myVoteState[holder.stickyPostion] = 0;
                            break;
                    }
                    //Log.d("Hello", "Arrow down clicked in comment, position is: "+String.valueOf(holder.stickyPostion)+" and votestate is: "+String.valueOf(myVoteState[holder.stickyPostion]));
                    String newVote = ""; //because we need to calculate the new score and the new vote based on the last previous vote

                    if (myVoteStateOrigin[holder.stickyPostion] == 0) {
                        newVote = String.valueOf(myVoteState[holder.stickyPostion]);
                    } else if (myVoteStateOrigin[holder.stickyPostion] == 1) {
                        newVote = String.valueOf(myVoteState[holder.stickyPostion] - 1);
                    } else if (myVoteStateOrigin[holder.stickyPostion] == -1) {
                        newVote = String.valueOf(myVoteState[holder.stickyPostion] + 1);
                    }
                    Log.d("hello", newVote);
                    String newScore = String.valueOf(Integer.parseInt(newVote) * 25);
                    myVoteStateOrigin[holder.stickyPostion] = myVoteState[holder.stickyPostion]; //because we then initialise the votestateorigin to the last state myvotestate


                    //Log.d("Hello", "Arrow up clicked in comment, position is: "+String.valueOf(holder.stickyPostion)+" and votestate is: "+String.valueOf(myVoteState[holder.stickyPostion]));
                    HttpRequestApp.addVote_Score(
                            Sign_User_Object.Email,
                            FilteredObjects.get(position).getAuthor(),
                            "Voted",
                            FilteredObjects.get(position).getObject(),
                            newScore,
                            newVote,
                            "Normal");
                }else {
                        Toast.makeText(context, "You cannot vote for yourself", Toast.LENGTH_SHORT).show();

                    }
                }

            });


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String Name = FilteredObjects.get(position).getName();
        String BottomText = FilteredObjects.get(position).getBottomText();
        int RightText = FilteredObjects.get(position).getVotes();
        int ImgViewID = FilteredObjects.get(position).getImgID();

        holder.stickyPostion = position; //because the convertiew is getting called on scroll with the correct position, but another position will be input during the click whitch is between 0 and 4, this can only but place outside and after having returned the convertview
        holder.NameTV.setText(Name);
        holder.BottomTV.setText(BottomText);
        holder.RightTV.setText(String.valueOf(RightText));
        //Log.d("Hello", "In get convertview commycustombaseadapter before switch, just for debugging, position is: "+String.valueOf(position)+" " +
        //         "and the votestate lengh is: "+String.valueOf(myVoteState.length));
        //if (myVoteState[position] == 0) {
        //Log.d("Hello", "correct just for debugging commycustombaseadpter");
        // }
        if (!(myVoteState.length <= position)) { //because when the user add more, object, it will be counted outside the length, user cannot vote for themselves
            switch (myVoteState[position]) { //because we are going to initialise the color, everytime the user scroll, or get the view

                case 0:
                    holder.upvoteComment.clearColorFilter();
                    holder.downvoteComment.clearColorFilter();
                    break;
                case 1:
                    holder.upvoteComment.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    holder.downvoteComment.clearColorFilter();
                    break;
                case -1:
                    holder.downvoteComment.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    holder.upvoteComment.clearColorFilter();
                    break;

            }
        }
        //holder.ImageViewIco.setImageResource(ImgViewID);
        //Log.d("Hello", "In get convertview commycustombaseadapter after switch, position is: "+String.valueOf(position)+" and votestate is: "+String.valueOf(myVoteState[position]));

        return convertView;
    }


    private class ViewHolder {
        TextView NameTV;
        TextView BottomTV;
        TextView RightTV;
        ImageView upvoteComment;
        ImageView downvoteComment;
        int stickyPostion;
    }


    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence arg0) {
            // TODO Auto-generated method stub
            FilterResults filterResults = new FilterResults();
            if (arg0 == null || arg0.length() == 0) {
                filterResults.values = MyArrayObjects;
                filterResults.count = MyArrayObjects.size();
            } else {
                String filterString = arg0.toString().toLowerCase();
                final ArrayList<Com_ItemObject> TempList = new ArrayList<Com_ItemObject>();
                for (Com_ItemObject Sis_ItemObject : MyArrayObjects) {
                    //Filters both from Name and Bottom Text
                    if ((Sis_ItemObject.getName() + " " + Sis_ItemObject.getBottomText()).toLowerCase().contains(filterString)) {
                        TempList.add(Sis_ItemObject);
                    }
                }

                filterResults.values = TempList;
                filterResults.count = TempList.size();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults arg1) {
            // TODO Auto-generated method stub
            FilteredObjects = (ArrayList<Com_ItemObject>) arg1.values;
            notifyDataSetChanged();
        }

    }

    @Override
    public boolean isEnabled(int position) { //because we want the listview to not be clickable
        return false;
    }

}
