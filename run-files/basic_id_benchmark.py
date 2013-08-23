#!/usr/bin/env python

"""
Generates a series of basic benchmarks based on the individual features
in Track 2 of the 2012 KDD Cup.

Usage: python basic_id_benchmark.py test_file training_file output_folder

Author: Ben Hamner (kdd2012@benhamner.com)
"""

import numpy as np

def process_training(training_file, id_loc):
    f = open(training_file)
    click_cnt = 0.0
    impression_cnt = 0.0
    id_fields = np.zeros((30000000,2))
    for line in f:
        line = line.strip().split("\t")
        click = float(line[0])
        impression = float(line[1])
	try:
        	id_val = int(line[id_loc-1])
	except IndexError:
		print line
        click_cnt += click
        impression_cnt += impression

        id_fields[id_val,0] += click
        id_fields[id_val,1] += impression
    mean_ctr = click_cnt / impression_cnt
    return id_fields, mean_ctr

def make_predictions(test_file, submission_file, mean_ctr, id_fields, id_loc):
    f_test = open(test_file)
    f_sub = open(submission_file, "w")
    for line in f_test:
        line = line.strip().split("\t")
        id_val = int(line[id_loc-3])
        if id_fields[id_val,1]>0:
            f_sub.write("%f\n" % (id_fields[id_val,0]/id_fields[id_val,1]))
        else:
            f_sub.write("%f\n" % mean_ctr)
    f_sub.close()

def make_mean_prediction(test_file, submission_file, mean_ctr):
    f_test = open(test_file)
    f_sub = open(submission_file, "w")
    for line in f_test:
        f_sub.write("%f\n" % mean_ctr)
    f_sub.close()

def main():
    import sys
    if len(sys.argv) != 4:
        print("Usage: python basic_id_benchmark.py " +
              "training_file test_file output_folder")
        sys.exit(1)
    training_file = sys.argv[1]
    test_file = sys.argv[2]
    output_folder = sys.argv[3]

    fields = [("ad", 4), ("advertiser", 5), ("query", 8), ("keyword", 9),
              ("title", 10), ("description", 11), ("user", 12)]
 
#    fields = [("description", 11)]
    for id_name, id_loc in fields:
        print("Making Predictions On %s" % id_name)
        id_fields, mean_ctr = process_training(training_file, id_loc)
        make_predictions(test_file,
                         output_folder + "/" + id_name + "_benchmark.csv",
                         mean_ctr, id_fields, id_loc)

    print("Making Mean Prediction")    
    make_mean_prediction(test_file,
                         output_folder + "/mean_benchmark.csv",
                         mean_ctr)

if __name__=="__main__":
    main()
