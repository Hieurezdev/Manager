import re
from datetime import datetime, timedelta

file_path = '/home/hungdvlper/Documents/NhapMonCNPM/Manager/src/main/java/com/example/manager/data.sql'

with open(file_path, 'r') as f:
    lines = f.readlines()

ctht_dict = {}
in_ctht = False
for line in lines:
    if line.startswith('INSERT INTO ChiTietHanhTrinh'):
        in_ctht = True
        continue
    if in_ctht and line.startswith('INSERT INTO '):
        in_ctht = False
    
    if in_ctht and re.match(r'^\(', line):
        parts = line.strip().strip(',;').strip('()').split(',')
        if len(parts) >= 4:
            thuTuGa = int(parts[1].strip())
            nhaGaId = int(parts[2].strip())
            hanhTrinhId = int(parts[3].strip())
            if hanhTrinhId not in ctht_dict:
                ctht_dict[hanhTrinhId] = []
            ctht_dict[hanhTrinhId].append((thuTuGa, nhaGaId))

for ht in ctht_dict:
    ctht_dict[ht].sort()

lich_trinh_dict = {}
in_lt = False
lt_id = 1
for line in lines:
    if line.startswith('INSERT INTO LichTrinh'):
        in_lt = True
        continue
    if in_lt and line.startswith('INSERT INTO '):
        in_lt = False
        
    if in_lt and re.match(r'^\(', line):
        # We split by comma, but be careful with spaces
        parts = line.strip().strip(',;').strip('()').split(',')
        if len(parts) >= 6:
            ngayKhoiHanh = parts[1].strip().strip("'")
            hanhTrinhId = int(parts[4].strip())
            lich_trinh_dict[lt_id] = (ngayKhoiHanh, hanhTrinhId)
            lt_id += 1

new_records = []
count = 0
for ltid in range(1, 100):
    if count >= 30: break
    if ltid == 2 or ltid == 35: continue
    if ltid not in lich_trinh_dict: continue
    
    ngayKhoiHanh_str, hanhTrinhId = lich_trinh_dict[ltid]
    if hanhTrinhId not in ctht_dict or not ctht_dict[hanhTrinhId]: continue
    
    ga_list = ctht_dict[hanhTrinhId]
    ga_di = ga_list[0][1]
    ga_den = ga_list[-1][1]
    
    dt = datetime.strptime(ngayKhoiHanh_str, '%Y-%m-%d %H:%M:%S')
    dt_den = dt + timedelta(hours=3)
    ngay_den_str = dt_den.strftime('%Y-%m-%d %H:%M:%S')
    
    new_records.append(f"('CTLT_{ltid:02d}_01', NULL, '{ngayKhoiHanh_str}', {ga_di}, {ltid})")
    new_records.append(f"('CTLT_{ltid:02d}_02', '{ngay_den_str}', NULL, {ga_den}, {ltid})")
    count += 1

for i in range(len(lines)):
    if "'CTLT_36_02'" in lines[i] and "35);" in lines[i]:
        lines[i] = lines[i].replace(';', ',')
        insert_idx = i + 1
        for j, rec in enumerate(new_records):
            terminator = ';' if j == len(new_records)-1 else ','
            lines.insert(insert_idx + j, rec + terminator + "\n")
        break

with open(file_path, 'w') as f:
    f.writelines(lines)
