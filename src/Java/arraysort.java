package Java;

import java.util.Comparator;

class arraysort implements Comparator<ListTokoLaporanToko>
{
    public int compare(ListTokoLaporanToko a, ListTokoLaporanToko b)
    {
        return Integer.parseInt(a.getTanggal()) - Integer.parseInt(b.getTanggal());
    }
}