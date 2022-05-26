OMP_THREAD_LIMIT=16 lstmtraining \
	--continue_from ../train/rus.lstm \
	--model_output ../output/pubg \
	--traineddata ../tessdata_best/rus.traineddata \
	--train_listfile ../train/rus.training_files.txt \
	--max_iterations 4000