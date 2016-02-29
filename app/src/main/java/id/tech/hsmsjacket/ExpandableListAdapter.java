package id.tech.hsmsjacket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by macbook on 2/15/16.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<RowData_History>> _listDataChild;
    private SharedPreferences sh;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<RowData_History>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final RowData_History childData = (RowData_History) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView tv_tgl = (TextView)convertView.findViewById(R.id.tv_tgl);

        TextView tv_transaction_id = (TextView)convertView.findViewById(R.id.tv_transaction_id);
        TextView tv_mason_id = (TextView)convertView.findViewById(R.id.tv_mason_id);
        TextView tv_qty= (TextView)convertView.findViewById(R.id.tv_qty);
        TextView tv_status = (TextView)convertView.findViewById(R.id.tv_status);
        View wrapper= (View)convertView.findViewById(R.id.wrapper);

        sh = _context.getSharedPreferences("sh_sms", Context.MODE_PRIVATE);
        String date_current = childData.dateArrived;
        String date_sh = sh.getString("tgl_now", "-");

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String date_now = df.format(Calendar.getInstance().getTime());

        tv_tgl.setText(" --- " + childData.dateArrived + " --- ");
        tv_mason_id.setText(childData.tv_mason_id);
        tv_transaction_id.setText(childData.tv_transaction_id);
        tv_qty.setText(childData.tv_qty);

        //set informasi status
        if(childData.tv_status.equals("1")){
            tv_status.setText("Menunggu konfirmasi");
            tv_status.setTextColor(Color.BLUE);
        }else {
            tv_status.setText("Terkonfirmasi");
            tv_status.setTextColor(Color.GREEN);
        }

        sh.edit().putString("tgl_now", date_current).commit();
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_header, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
