# create_linechart.py
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

def plot_line_chart(directory_path):
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

        # Creating the line chart
        plt.figure(figsize=(10, 5))
        plt.plot(data.index, data['Number_of_Incidents'], marker='o', linestyle='-')

        plt.title('Number of Incidents Over Time')
        plt.xlabel('Date')
        plt.ylabel('Number of Incidents')
        plt.grid(True)
        plt.tight_layout()

        # Save the plot as a PNG file
        plot_file_name = os.path.join(directory_path, 'incidents_line_chart.png')
        plt.savefig(plot_file_name)
        logging.info(f"Line chart successfully saved at {plot_file_name}")

        plt.show()
    except Exception as e:
        logging.error(f"Failed to create or save the line chart: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        logging.error("Usage: python3 create_linechart.py 'path_to_directory'")
    else:
        plot_line_chart(sys.argv[1])
