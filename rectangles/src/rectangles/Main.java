package rectangles;

import java.util.*;
import java.io.*;

class Rectangle {
	public int x_left_lower = 0, y_left_lower = 0;
	public int x_right_upper = 0, y_right_upper = 0;
	
	public Rectangle(int x_left_lower, int y_left_lower, int x_right_upper, int y_right_upper) {
		this.x_left_lower = x_left_lower;
		this.y_left_lower = y_left_lower;
		this.x_right_upper = x_right_upper;
		this.y_right_upper = y_right_upper;
	}
	
	public Rectangle() {}
}


class Segment {
	public int l = 0, r = 0;
	
	public Segment(int l, int r) {
		this.l = l;
		this.r = r;
	}
}


class SegmentSet
{
	public ArrayList<Segment> segments = null;
	
	public SegmentSet() {
		this.segments = new ArrayList<Segment>();
	}
	
	public void add(Segment sgt) {
		int id = segments.size();
		segments.add(sgt);
		while (id > 0) {
			if (segments.get(id).l < segments.get(id - 1).l) {
				Segment tmp = segments.get(id);
				segments.set(id, segments.get(id - 1));
				segments.set(id - 1, tmp);
				id--;
			} else {
				break;
			}
		}
	}
	
	public int coversLength() {
		int res = 0;
		int l = -1, r = -1;
		boolean first = true;
		
		for (Segment seg: segments) {
			if (first) {
				l = seg.l;
				r = seg.r;
				first = false;
			} else {
				if (seg.l > r) {
					res += r - l;
					l = seg.l;
					r = seg.r;
				} else {
					r = Math.max(seg.r, r);
				}
			}
		}
		
		if (!first) {
			res += r - l;
		}
		
		return res;
	}
	
	public void remove(Segment seg) {
		for (int i = 0; i < segments.size(); i++) {
			if (segments.get(i).l == seg.l && segments.get(i).r == seg.r) {
				segments.remove(i);
				return;
			}
		}
	}
}


public class Main {
	
	
	
	public static void main(String[] args) {
		try {
			InputStreamReader source = null;
			if (args.length == 0) {
				source = new InputStreamReader(System.in);
			} else if (args[0].equals("console")) {
				source = new InputStreamReader(System.in);
			} else {
				source = new InputStreamReader(new FileInputStream(args[0]));
			}
			ArrayList<Rectangle> rects = read(source);
			System.out.println(solve(rects));
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
    }
	
	
	public static int solve(ArrayList<Rectangle> rects) {
		class SideOfRect implements Comparable<SideOfRect> {
			public boolean left = true;
			public Rectangle rect;
			
			public SideOfRect(Rectangle rect, boolean left) {
				this.left = left;
				this.rect = rect;
			}

			@Override
			public int compareTo(SideOfRect s) {
				int val_this = -1, val_s = -1;
				
				
				if (this.left) {
					val_this = this.rect.x_left_lower;
				} else {
					val_this = this.rect.x_right_upper;
				}
				
				if (s.left) {
					val_s = s.rect.x_left_lower;
				} else {
					val_s = s.rect.x_right_upper;
				}
				
				
				if (val_this > val_s)
					return 1;
				else if (val_this == val_s)
					return 0;
				else
					return -1;
			}
		}
		
		ArrayList<SideOfRect> sides = new ArrayList<SideOfRect>();
		for (Rectangle rect : rects) {
			sides.add(new SideOfRect(rect, true));
			sides.add(new SideOfRect(rect, false));
		}
		
		Collections.sort(sides);
		
		int lastChanged = -1;
		boolean firstChangedFlag = true;
		int res = 0;
		SegmentSet activeSet = new SegmentSet();
		for (var side: sides) {
			if (side.left) {
				if (firstChangedFlag) {
					firstChangedFlag = false;
					activeSet.add(new Segment(side.rect.y_left_lower, side.rect.y_right_upper));
					lastChanged = side.rect.x_left_lower;
				} else {
					res += activeSet.coversLength() * (side.rect.x_left_lower - lastChanged);
					activeSet.add(new Segment(side.rect.y_left_lower, side.rect.y_right_upper));
					lastChanged = side.rect.x_left_lower;
				}
			} else {
				res += activeSet.coversLength() * (side.rect.x_right_upper - lastChanged);
				activeSet.remove(new Segment(side.rect.y_left_lower, side.rect.y_right_upper));
				lastChanged = side.rect.x_right_upper;
			}
		}
		
		return res;
	}
	
	
	public static ArrayList<Rectangle> read(InputStreamReader sourceReader) throws Exception {
		ArrayList<Rectangle> res = new ArrayList<Rectangle>();
		
		// Assume the number of rectangles is entered to the console
		// and the rectangles are entered line by line
		try (BufferedReader reader = new BufferedReader(sourceReader)) {
			int num = 0;
			try {
				num = Integer.parseInt(reader.readLine());
			} catch(NumberFormatException ex) {
				num = 0;
				System.out.println(ex.getMessage());
			}
			
			while (num > 0) {
				String line = reader.readLine();
				List<String> coords = Arrays.asList(line.split("\\s*\\s"));
				if (coords.size() < 4) {
					throw new Exception("Parsing error 1");
				}
				
				try {
					Rectangle rect = new Rectangle();
					rect.x_left_lower = Integer.parseInt(coords.get(0));
					rect.y_left_lower = Integer.parseInt(coords.get(1));
					rect.x_right_upper = Integer.parseInt(coords.get(2));
					rect.y_right_upper = Integer.parseInt(coords.get(3));
					
					
					res.add(rect);
				} catch(NumberFormatException ex) {
					System.out.println(ex.getMessage());
					throw new Exception("Parsing error 2");
				}
				num--;
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			throw new Exception("Parsing error");
		}
		
		return res;
	}
}
