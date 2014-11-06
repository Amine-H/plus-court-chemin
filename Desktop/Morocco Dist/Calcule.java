import java.util.ArrayList;


public class Calcule
{
	private static double distanceBetween(DataHolder[] data,int from,int to)
	{
		for(int i = 0;i < data.length;i++)
		{
			if((data[i].getCity1() == from && data[i].getCity2()   == to) ||
			  (data[i].getCity2()  == from && data[i].getCity1()   == to))
			{
				return data[i].getDistance();
			}
		}
		return 0;
	}

	public static float totalKm(DataHolder[] data,ArrayList<Integer> v)
	{
		float total = 0;
		if(v.size() > 1)
		{
			for(int i = 0;i < v.size() - 1;i++)
			{
				total += distanceBetween(data,v.get(i),v.get(i + 1));
			}
		}
		return total;
	}

	private static boolean existsIn(int v,ArrayList<Integer> array)
	{
		for(int i = 0;i < array.size();i++)
		{
			if(v == array.get(i))
				return true;
		}
		return false;
	}

	public static ArrayList<Integer> findPath(int from,int to,DataHolder[] data,ArrayList<Integer> v)
	{
		ArrayList<Integer> tab;

		if(v == null)
		{
			tab = new ArrayList<Integer>();
		}
		else
		{
			tab = (ArrayList<Integer>) v.clone();
		}

		for(int i = 0;i < data.length;i++)
		{
			if((data[i].getCity1() == from && data[i].getCity2() == to) ||
			  ( data[i].getCity2() == from && data[i].getCity1() == to))
			{
				tab.add(from);
				tab.add(to);
				return tab;
			}
		}

		float min = -1;
		float dist;

		tab.add(from);
		ArrayList<Integer> aIntMin = null,aIntDist = null;
		for(int i = 0;i < data.length;i++)
		{
			if(data[i].getCity1() == from && !existsIn(data[i].getCity2(),tab))
			{
				aIntDist = findPath(data[i].getCity2(),to,data,tab);
				if(aIntDist == null)
				{
					continue;
				}
				dist = totalKm(data,aIntDist);
				if(min < 0 || dist < min)
				{
					min = dist;
					aIntMin = aIntDist;
				}
				continue;//sauter le test dessous (c'est claire qu'il est 'false')
			}
			if(data[i].getCity2() == from && !existsIn(data[i].getCity1(),tab))
			{
				aIntDist = findPath(data[i].getCity1(),to,data,tab);
				if(aIntDist == null)
				{
					continue;
				}
				dist = totalKm(data,aIntDist);
				if(min < 0 || dist < min)
				{
					min = dist;
					aIntMin = aIntDist;
				}
			}
		}
		return aIntMin;
	}
}