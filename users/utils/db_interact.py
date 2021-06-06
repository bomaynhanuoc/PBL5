from aiproject.firebase_config import database, storage
from users.recognition.training import generate_vector
import os


def save_to_db(mode='default', created_new_file=None):
    path_to_local_images = os.path.join(os.getcwd(), 'Data', "")
    df = generate_vector(path_to_local_images)
    dirpath, _, filenames = next(os.walk(path_to_local_images))
    count = 0

    if mode == 'init':
        for f in filenames:
            abs_path = os.path.abspath('{0}/{1}'.format(dirpath, f))
            row = df.iloc[count].to_dict()

            res = storage.child(f).put(abs_path)
            imageUrl = storage.child(f).get_url(res['downloadTokens'])

            data = {
                'imageName': f,
                'imageUrl': imageUrl,
                'name': f.split('.')[0],
                'vector': row
            }
            database.child("IDs").child("user{0}".format(count)).set(data)
            count += 1
    elif mode == 'create' and created_new_file != None:
        database.child('IDs').remove()
        for f in filenames:
            abs_path = os.path.abspath('{0}/{1}'.format(dirpath, f))
            row = df.iloc[count].to_dict()

            if f == created_new_file:
                res = storage.child(created_new_file).put(abs_path)
                imageUrl = storage.child(created_new_file).get_url(
                    res['downloadTokens'])
                data = {
                    'imageName': created_new_file,
                    'imageUrl': imageUrl,
                    'name': created_new_file.split('.')[0],
                    'vector': row
                }

                database.child("IDs").child("user{0}".format(count)).set(data)
            else:
                data = {
                    'imageName': f,
                    'imageUrl': storage.child(f).get_url(token=None),
                    'name': f.split('.')[0],
                    'vector': row,
                }
                database.child("IDs").child("user{0}".format(count)).set(data)
            count += 1
        pass
    else:
        database.child('IDs').remove()

        for f in filenames:
            abs_path = os.path.abspath('{0}/{1}'.format(dirpath, f))
            row = df.iloc[count].to_dict()

            data = {
                'imageName': f,
                'imageUrl': storage.child(f).get_url(token=None),
                'name': f.split('.')[0],
                'vector': row,
            }

            database.child("IDs").child("user{0}".format(count)).set(data)
            count += 1
