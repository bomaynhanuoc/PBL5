from aiproject.firebase_config import database, storage
from users.recognition.training import generate_vector
import pandas as pd
import os
import json


def save_to_db(mode='default', created_new_file=None):
    path_to_local_images = os.path.join(os.getcwd(), 'Data', "")
    df = generate_vector(path_to_local_images)
    dirpath, _, filenames = next(os.walk(path_to_local_images))
    count = 0

    if mode == 'init':
        for f in filenames:
            abs_path = os.path.abspath('{0}/{1}'.format(dirpath, f))
            row = df.iloc[count].to_dict()

            # save img into storage
            res = storage.child(f).put(abs_path)
            imageUrl = storage.child(f).get_url(res['downloadTokens'])

            # save data infomation in realtime db
            data = {
                'imageName': f,
                'imageUrl': imageUrl,
                'name': f.split('.')[0],
                'vector': row
            }
            database.child("IDs").push(data)
            count += 1
    elif mode == 'create' and created_new_file != None:
        for f in filenames:
            abs_path = os.path.abspath('{0}/{1}'.format(dirpath, f))
            row = df.iloc[count].to_dict()

            if f == created_new_file:
                # save img into storage
                res = storage.child(created_new_file).put(abs_path)
                imageUrl = storage.child(created_new_file).get_url(
                    res['downloadTokens'])
                # save data infomation in realtime db
                data = {
                    'imageName': created_new_file,
                    'imageUrl': imageUrl,
                    'name': created_new_file.split('.')[0],
                    'vector': row
                }

                database.child("IDs").push(data)
            else:
                print(f)
                # update data infomation in realtime db
                database.child("IDs").update({'vector': row})
            count += 1
        pass
    else:
        for f in filenames:
            abs_path = os.path.abspath('{0}/{1}'.format(dirpath, f))
            row = df.iloc[count].to_dict()
            # update data infomation in realtime db
            database.child("IDs").update({'vector': row})
            count += 1
