package data.mealguru;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import edible.DailyIntake;
import edible.Meal;
import gui.PrimaryWindow;
import utility.DataFormat;

public class DailyIntakeDA extends JDBC {

	/**
	 * This method is used to save an instance of a DailyIntake.
	 * 
	 * @param dailyIntake
	 * @return The ID of the record in the mealguru.db SQL file.
	 */
	public int saveDailyIntake(DailyIntake dailyIntake) {

		int dailyIntakeID = 0;

		try {

			Connection databaseConnection = super.getMysqlConnection();
			Statement sqlStatement = databaseConnection.createStatement();

			for (Meal meal : dailyIntake.getMeals()) {

				String SQLStr = "INSERT INTO DailyIntake (user_id, meal_id, date) " + "VALUES ("
						+ PrimaryWindow.getActiveUser().getID() + ", " + meal.getID() + ", '"
						+ DataFormat.transformDateToString(dailyIntake.getDate()) + "')";

				sqlStatement.execute(SQLStr);

				ResultSet res = sqlStatement.executeQuery("select last_insert_rowid() newid;");

				if (res.next())
					dailyIntakeID = res.getInt(1);

			}

			sqlStatement.close();
			databaseConnection.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return dailyIntakeID;

	}

	public int saveMealToDay(Meal meal, Date date) {

		int dailyIntakeID = 0;

		try {

			Connection databaseConnection = super.getMysqlConnection();
			Statement sqlStatement = databaseConnection.createStatement();

			String SQLStr = "INSERT INTO DailyIntake (user_id, meal_id, date) " + "VALUES ("
					+ PrimaryWindow.getActiveUser().getID() + ", " + meal.getID() + ", '"
					+ DataFormat.transformDateToString(date) + "')";

			sqlStatement.execute(SQLStr);

			ResultSet res = sqlStatement.executeQuery("select last_insert_rowid() newid;");

			if (res.next())
				dailyIntakeID = res.getInt(1);

			sqlStatement.close();
			databaseConnection.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return dailyIntakeID;

	}

	public DailyIntake getDailyIntakeByDay(Date date) {

		return this.getDailyIntakeByDay(DataFormat.transformDateToString(date));

	}

	public DailyIntake getDailyIntakeByDay(String date) {

		DailyIntake dailyIntake = new DailyIntake(DataFormat.transformStringToDate(date));

		try {

			Connection databaseConnection = super.getMysqlConnection();
			Statement sqlStatement = databaseConnection.createStatement();

			String SQLStr = "SELECT * FROM DailyIntake WHERE date = '" + date + "' AND user_id = "
					+ PrimaryWindow.getActiveUser().getID() + ";";

			ResultSet res = sqlStatement.executeQuery(SQLStr);

			ArrayList<Meal> arrayListOfMeals = new ArrayList<>();
			MealDA mealDA = new MealDA();

			while (res.next()) {

				Meal meal = mealDA.findMealById(res.getInt("meal_id"));
				arrayListOfMeals.add(meal);

			}

			dailyIntake.addMeals(arrayListOfMeals);

			sqlStatement.close();
			databaseConnection.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return dailyIntake;

	}

	public ArrayList<Meal> getMealsByDay(Date date) {

		return this.getMealsByDay(DataFormat.transformDateToString(date));

	}

	public ArrayList<Meal> getMealsByDay(String date) {

		ArrayList<Meal> arrayListOfMeals = new ArrayList<>();

		try {

			Connection databaseConnection = super.getMysqlConnection();
			Statement sqlStatement = databaseConnection.createStatement();

			String SQLStr = "SELECT * FROM DailyIntake WHERE date = '" + date + "';";

			ResultSet res = sqlStatement.executeQuery(SQLStr);

			MealDA mealDA = new MealDA();
			while (res.next()) {

				Meal meal = mealDA.findMealById(res.getInt("meal_id"));
				arrayListOfMeals.add(meal);

			}

			sqlStatement.close();
			databaseConnection.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return arrayListOfMeals;

	}

	public int getNumberOfMealsBetween(Date from, Date to) {

		int toReturn = 0;

		try {

			Connection databaseConnection = super.getMysqlConnection();
			Statement sqlStatement = databaseConnection.createStatement();

			String SQLStr = "SELECT id FROM DailyIntake WHERE date >= '" + DataFormat.transformDateToString(from)
					+ "' AND date <= '" + DataFormat.transformDateToString(to) + "';";

			ResultSet res = sqlStatement.executeQuery(SQLStr);

			while (res.next())
				toReturn++;

			sqlStatement.close();
			databaseConnection.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return toReturn;

	}

	public ArrayList<Meal> getMealsBetweenDays(Date from, Date to) {

		return this.getMealsBetweenDays(DataFormat.transformDateToString(from), DataFormat.transformDateToString(to));

	}

	public ArrayList<Meal> getMealsBetweenDays(String from, String to) {

		ArrayList<Meal> arrayListOfMeals = new ArrayList<>();

		try {

			Connection databaseConnection = super.getMysqlConnection();
			Statement sqlStatement = databaseConnection.createStatement();

			String SQLStr = "SELECT * FROM DailyIntake WHERE date >= '" + from + "' AND date <= '" + to
					+ "' AND user_id = " + PrimaryWindow.getActiveUser().getID() + ";";

			ResultSet res = sqlStatement.executeQuery(SQLStr);

			MealDA mealDA = new MealDA();
			while (res.next()) {

				Meal meal = mealDA.findMealById(res.getInt("meal_id"));
				arrayListOfMeals.add(meal);

			}

			sqlStatement.close();
			databaseConnection.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return arrayListOfMeals;

	}

	public void deleteAllFrom(Date date) {

		this.deleteAllFrom(DataFormat.transformDateToString(date));

	}

	public void deleteAllFrom(String date) {

		try {

			Connection databaseConnection = super.getMysqlConnection();
			Statement sqlStatement = databaseConnection.createStatement();

			String SQLStr = "DELETE FROM DailyIntake WHERE date = '" + date + "' AND " + "user_id = "
					+ PrimaryWindow.getActiveUser().getID() + ";";

			sqlStatement.execute(SQLStr);

			sqlStatement.close();
			databaseConnection.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void deleteMealFromDay(Meal meal, Date date) {

		try {

			Connection databaseConnection = super.getMysqlConnection();
			Statement sqlStatement = databaseConnection.createStatement();

			

			String sqlString = "DELETE FROM DailyIntake WHERE DailyIntake.id IN "
					+ "(SELECT DailyIntake.id FROM DailyIntake WHERE " + "date = '"
					+ DataFormat.transformDateToString(date) + "' AND " + "meal_id = " + meal.getID() + " AND "
					+ "user_id = " + PrimaryWindow.getActiveUser().getID() + " LIMIT 1);";

			sqlStatement.execute(sqlString);

			sqlStatement.close();
			databaseConnection.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
