import tempfile
import flatbuffers
from flask import Flask, send_file
import person

app = Flask(__name__)


@app.route("/", methods=["GET", "POST"])
def flatbuffer():
    builder = flatbuffers.Builder(1024)
    name = builder.CreateString('John Smith')
    # Create the first person
    person.PersonStart(builder)
    person.PersonAddName(builder, name)
    person.PersonAddAge(builder, 10)
    first_person = person.PersonEnd(builder)
    builder.Finish(first_person)
    buffer = builder.Output()
    f = tempfile.TemporaryFile(mode="rb+")
    f.write(buffer)
    f.seek(0)
    return send_file(f, as_attachment=True,
                     attachment_filename='file.dat')


if __name__ == '__main__':
    app.run(host='192.168.68.102')
