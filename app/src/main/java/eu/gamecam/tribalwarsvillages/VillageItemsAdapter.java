package eu.gamecam.tribalwarsvillages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gamecam on 11.1.2018.
 */

public class VillageItemsAdapter extends BaseAdapter {
    private ArrayList<Village> listData;
    private LayoutInflater layoutInflater;
    private ArrayList<Village> newVil;

    public VillageItemsAdapter(Context aContext, ArrayList<Village> listData, ArrayList<Village> newVillage) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        newVil=newVillage;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row, null);
            holder = new ViewHolder();
            holder.nameVillage = (TextView) convertView.findViewById(R.id.vill_name);
            holder.coordinates = (TextView) convertView.findViewById(R.id.coordinates);
            holder.points = (TextView) convertView.findViewById(R.id.points);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        boolean giveText=false;
        for (int i=0; i<newVil.size(); i++)
        {
        if (listData.get(position).getId()==newVil.get(i).getId()) giveText=true;
        }
        if (giveText==true) {
            try {holder.nameVillage.setText(listData.get(position).getName()+" (Nová dedina)");} catch (NullPointerException ex) {}
            try {holder.coordinates.setText(listData.get(position).getX()+"|"+listData.get(position).getY());} catch (NullPointerException ex) {}
            try {holder.points.setText(String.valueOf(listData.get(position).getPoints()));} catch (NullPointerException ex) {}
        }
        else {
            try {
                holder.nameVillage.setText(listData.get(position).getName());
            } catch (NullPointerException ex) {
            }
            try {
                holder.coordinates.setText(listData.get(position).getX() + "|" + listData.get(position).getY());
            } catch (NullPointerException ex) {
            }
            try {
                holder.points.setText(String.valueOf(listData.get(position).getPoints()));
            } catch (NullPointerException ex) {
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView nameVillage;
        TextView coordinates;
        TextView points;
    }
}
