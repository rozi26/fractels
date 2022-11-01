import sys
import os
from os import listdir
import cv2

workingdirectory=os.getcwd()

folder = sys.argv[1]
dest = sys.argv[2]
fps = int(sys.argv[3])
files = listdir(folder)


frame = cv2.imread(os.path.join(folder, files[0]))
height, width, layers = frame.shape

video = cv2.VideoWriter(filename=dest,fourcc=0,frameSize=(width,height),fps=fps)

for image in files:
    video.write(cv2.imread(os.path.join(folder, image)))

cv2.destroyAllWindows()
video.release()
print("python done")