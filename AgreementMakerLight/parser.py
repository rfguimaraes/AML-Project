import re
import sys

def parse(filename):
    with open(filename, 'r') as data:
        table = dict()
        interest = re.compile("^e4a ")
        flag = True
        for line in data:
            if interest.match(line):
                line = line.strip()
                if flag:
                    head_tokens = line.split(' ')
                    flag = False
                else:
                    val_tokens = line.replace('%', '').replace('\t', ' ').split(' ')
                    test = head_tokens[1]
                    strategy = ' '.join(head_tokens[2:-1])
                    try:
                        prec = val_tokens[1]
                        rec = val_tokens[2]
                        f1 = val_tokens[3]
                    except:
                        print(val_tokens)

                    res = ','.join([test, strategy, prec, rec, f1])
                    print(res)
                    flag = True

if __name__ == '__main__':
    parse(sys.argv[1])
            
