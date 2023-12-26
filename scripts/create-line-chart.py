# create_graphs.py
import sys
import os
import pandas as pd
import matplotlib.pyplot as plt
import logging

# Setup basic logging
logging.basicConfig(level=logging.INFO)

# Find a single CSV file in the given directory
def find_csv_filename(path):
    for file in os.listdir(path):
        if file.endswith('.csv'):
            return os.path.join(path, file)  # return full path of the file
    return None

def plot_data(directory_path):
    csv_file = find_csv_filename(directory_path)
    if csv_file is None:
        logging.error("No CSV file found in the directory.")
        return

    # Log the found CSV file
    logging.info(f"Found CSV file: {csv_file}")

    # Read the CSV file
    try:
        data = pd.read_csv(csv_file, delimiter='|')
        logging.info("CSV file successfully read")
    except Exception as e:
        logging.error(f"Failed to read the CSV file: {e}")
        return

    # Combine year and month into a single date and set as index
    try:
        data['Date'] = pd.to_datetime(data['year'].astype(str) + '-' + data['month'].astype(str), format='%Y-%m')
        data.set_index('Date', inplace=True)
        logging.info("Data processing successful")
    except Exception as e:
        logging.error(f"Data processing failed: {e}")
        return

    # Plotting the data
    try:
        plt.figure(figsize=(10, 5))
        plt.plot(data.index, data['Number_of_Incidents'], marker='o', linestyle='-')
        plt.title('Number of Incidents Over Time')
        plt.xlabel('Date')
        plt.ylabel('Number of Incidents')
        plt.grid(True)
        plt.tight_layout()

        # Constructing file name for the plot
        plot_file_name = os.path.join(directory_path, 'incidents_over_time.png')
        plt.savefig(plot_file_name)
        logging.info(f"Plot successfully saved at {plot_file_name}")

    except Exception as e:
        logging.error(f"Failed to create or save the plot: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        logging.error("Usage: python3 create-graphs.py 'path_to_directory'")
    else:
        plot_data(sys.argv[1])
