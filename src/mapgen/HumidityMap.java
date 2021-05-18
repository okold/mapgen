package mapgen;

import java.awt.Point;

/**
 * A map which holds humidity data in each cell.
 * 
 * Note that this code is messy. I've included it in the final project as it
 * works... sort of, but if I were to redo it today, I'd scrap it all and start
 * from scratch.
 * 
 * Cells at sea level are considered to have 100% humidity. The direction and
 * strength of the wind that carries this humidity depends on the zone (horse
 * winds, trade winds, polar cells, etc). If the terrain rises, then the %
 * humidity rises as well, but if the terrain falls, the humidity % drops. This
 * creates a rain shadow effect.
 * 
 * @author Olga Koldachenko
 */
public class HumidityMap extends Map {
	private double[][] humidity_map;

	HumidityMap() {
		super();
		humidity_map = new double[DEFAULT_WIDTH + 1][DEFAULT_HEIGHT + 1];
	}

	HumidityMap(int scale) {
		super(scale);
		humidity_map = new double[getWidth()][getHeight()];
	}

	public double getHumidity(Point p) {
		return humidity_map[getXAdj(p.x)][getYAdj(p.y)];
	}

	public double getHumidity(int x, int y) {
		return humidity_map[getXAdj(x)][getYAdj(y)];
	}

	/**
	 * Calculates humidity based on the given parameters. The height map for
	 * terrain, the tilt to get the different wind zones, the water level, and a
	 * strength which determines how much one cell influences its neighbours.
	 * 
	 * @param height_map
	 * @param tilted_planet
	 * @param water_level
	 * @param wind_strength
	 */
	public void generate(HeightMap height_map, TiltedPlanet tilted_planet, int water_level, double wind_strength) {
		int polar_height, horse_wind_height, trade_wind_height;
		int last_row = getHeight() - 1;
		int midpoint = getHeight() / 2;

		// gets the horse wind and trade wind positions
		if (tilted_planet.arcticAtPoles()) {
			polar_height = getY(tilted_planet.getArcticCircle());
			horse_wind_height = getY(tilted_planet.getNorthernTropic() - 2);
			trade_wind_height = getY(tilted_planet.getNorthernTropic() + 2);

		} else {
			polar_height = getY(tilted_planet.getNorthernTropic());
			horse_wind_height = getY(tilted_planet.getArcticCircle() - 2);
			trade_wind_height = getY(tilted_planet.getArcticCircle() + 2);
		}

		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (height_map.getAltitude(new Point(x, y)) <= water_level) {
					humidity_map[x][y] = 1;
				} else {
					double lat = getLatitude(y);

					if (lat < 0) {
						lat *= -1;
					}

					if (lat >= getLatitude(horse_wind_height)) {
						lat -= getLatitude(horse_wind_height);
						lat = (lat / getLatitude(horse_wind_height));
					} else {

						lat = ((getLatitude(horse_wind_height) - lat) / getLatitude(horse_wind_height));
					}

					humidity_map[x][y] = lat;
				}
			}
		}

		// Polar Cells
		for (int y = 1; y < polar_height; y++) {
			int bottom_y = last_row - y;

			for (int x = getWidth() - 1; x >= 0; x--) {
				Point p = new Point(x, y);
				int x_prev = getXAdj(x + 1);
				int x_b = getXAdj(x + 2);

				if (height_map.getAltitude(p) <= water_level) {
					humidity_map[x][y] = 1;
				} else {
					double avg = (humidity_map[x][y] + humidity_map[x][y - 1] + humidity_map[x_prev][y - 1]
							+ humidity_map[x_b][y - 1]) / 4;

					humidity_map[x][y] = wind_strength * avg;
				}

				if (height_map.getAltitude(new Point(x, bottom_y)) <= water_level) {
					humidity_map[x][bottom_y] = 1;
				} else {
					double avg = (humidity_map[x][bottom_y] + humidity_map[x][bottom_y + 1]
							+ humidity_map[x_prev][bottom_y + 1] + humidity_map[x_b][bottom_y + 1]) / 4;

					humidity_map[x][bottom_y] = wind_strength * avg;
				}
			}
		}

		// Westerlies
		for (int y = horse_wind_height - 1; y >= polar_height; y--) {
			int bottom_y = last_row - y;

			for (int x = 0; x < getWidth(); x++) {
				int x_prev = getXAdj(x - 1);
				int x_b = getXAdj(x - 2);

				if (height_map.getAltitude(new Point(x, y)) <= water_level) {
					humidity_map[x][y] = 1;
				} else {
					double avg = (humidity_map[x][y] + humidity_map[x][y + 1] + humidity_map[x_prev][y + 1]
							+ humidity_map[x_b][y + 1]) / 4;

					humidity_map[x][y] = wind_strength * avg;

					int h1 = height_map.getAltitude(new Point(x, y));
					int h2 = height_map.getAltitude(new Point(x_prev, y + 1));

					if (h1 < h2) {
						humidity_map[x][y] *= ((humidity_map[x][y] + ((h2 - h1) / HeightMap.MAX_HEIGHT)) / 2);
					}
				}

				if (height_map.getAltitude(new Point(x, bottom_y)) <= water_level) {
					humidity_map[x][bottom_y] = 1;
				} else {
					double avg = (humidity_map[x][bottom_y] + humidity_map[x][bottom_y - 1]
							+ humidity_map[x_prev][bottom_y - 1] + humidity_map[x_b][bottom_y - 1]) / 4;

					humidity_map[x][bottom_y] = wind_strength * avg;

					int h1 = height_map.getAltitude(new Point(x, bottom_y));
					int h2 = height_map.getAltitude(new Point(x_prev, bottom_y - 1));

					if (h1 < h2) {
						humidity_map[x][bottom_y] *= ((humidity_map[x][bottom_y] + ((h2 - h1) / HeightMap.MAX_HEIGHT))
								/ 2);
					}
				}
			}
		}

		// Trade Winds / Tropical Zones
		for (int y = trade_wind_height; y <= midpoint; y++) {
			int bottom_y = last_row - y;

			for (int x = getWidth() - 1; x >= 0; x--) {
				Point p = new Point(x, y);
				int x_prev = getXAdj(x + 1);
				int x_b = getXAdj(x + 2);

				if (height_map.getAltitude(p) <= water_level) {
					humidity_map[x][y] = 1;
				} else {
					double avg = (humidity_map[x][y] + humidity_map[x][y - 1] + humidity_map[x_prev][y - 1]
							+ humidity_map[x_b][y - 1]) / 4;

					humidity_map[x][y] = wind_strength * avg;

					int h1 = height_map.getAltitude(new Point(x, y));
					int h2 = height_map.getAltitude(new Point(x_prev, y + 1));

					if (h1 > h2) {
						humidity_map[x][y] /= ((humidity_map[x][y] + ((h2 - h1) / HeightMap.MAX_HEIGHT)) / 2);
					}

					if (humidity_map[x][y] > 0.5) {
						humidity_map[x][y] = (humidity_map[x][y] + 1) / 2;
					}
				}

				if (height_map.getAltitude(new Point(x, bottom_y)) <= water_level) {
					humidity_map[x][bottom_y] = 1;
				} else {
					double avg = (humidity_map[x][bottom_y] + humidity_map[x][bottom_y + 1]
							+ humidity_map[x_prev][bottom_y + 1] + humidity_map[x_b][bottom_y + 1]) / 4;

					humidity_map[x][bottom_y] = wind_strength * avg;

					int h1 = height_map.getAltitude(new Point(x, bottom_y));
					int h2 = height_map.getAltitude(new Point(x_prev, bottom_y + 1));

					if (h1 > h2) {
						humidity_map[x][bottom_y] /= ((humidity_map[x][bottom_y] + ((h2 - h1) / HeightMap.MAX_HEIGHT))
								/ 2);
					}

					if (humidity_map[x][bottom_y] > 0.5) {
						humidity_map[x][bottom_y] = (humidity_map[x][bottom_y] + 1) / 2;
					}
				}
			}
		}
	}
}