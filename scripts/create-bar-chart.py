# create_barchart.py
import sys
import os
import pandas as pd
import matplotlib.pyplot as plt
import logging

logging.basicConfig(level=logging.INFO)

def find_csv_filename(path):
    for file in os.listdir(path):
        if file.endswith('.csv'):
            return os.path.join(path, file)
    return None

def plot_bar_chart(directory_path):
    csv_file = find_csv_filename(directory_path)
    if csv_file is None:
        logging.error("No CSV file found in the directory.")
        return

    logging.info(f"Found CSV file: {csv_file}")

    try:
        data = pd.read_csv(csv_file, delimiter='|')
        logging.info("CSV file successfully read")
    except Exception as e:
        logging.error(f"Failed to read the CSV file: {e}")
        return

    try:
        data['Date'] = pd.to_datetime(data['year'].astype(str) + '-' + data['month'].astype(str), format='%Y-%m')
        data.set_index('Date', inplace=True)

        data['Number_of_Incidents'].plot(kind='bar', figsize=(10, 5))
        plt.title('Number of Incidents per Month')
        plt.xlabel('Date')
        plt.ylabel('Number of Incidents')
        plt.tight_layout()

        plot_file_name = os.path.join(directory_path, 'incidents_bar_chart.png')
        plt.savefig(plot_file_name)
        logging.info(f"Bar chart successfully saved at {plot_file_name}")

        plt.show()
    except Exception as e:
        logging.error(f"Failed to create or save the bar chart: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        logging.error("Usage: python3 create_barchart.py 'path_to_directory'")
    else:
        plot_bar_chart(sys.argv[1])
