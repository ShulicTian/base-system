
package org.ks365.osmp.sys.service;

import org.ks365.osmp.common.utils.StringUtils;
import org.ks365.osmp.sys.dao.GenerateDao;
import org.ks365.osmp.sys.entity.GenerateEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 代码生成service
 *
 * @author tianslc
 */
@Service
@Transactional(readOnly = true)
public class GenerateService {
    private final GenerateDao generateDao;

    public GenerateService(GenerateDao generateDao) {
        this.generateDao = generateDao;
    }

    @Cacheable(value = "generateList", key = "'all'")
    public List<GenerateEntity> findAllList() {
        return generateDao.findAll();
    }

    public Page<GenerateEntity> getPageByColumn(GenerateEntity generateEntity) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        Example<GenerateEntity> example = Example.of(generateEntity, exampleMatcher);
        return generateDao.findAll(example, PageRequest.of(generateEntity.getPage() - 1, generateEntity.getSize(), Sort.by("createDate")));
    }

    public List<GenerateEntity> getListByColumn(GenerateEntity generateEntity) {
        Example<GenerateEntity> example = Example.of(generateEntity);
        return generateDao.findAll(example, Sort.by("createDate"));
    }

    public GenerateEntity getOneByColumn(GenerateEntity generateEntity) {
        Example<GenerateEntity> example = Example.of(generateEntity);
        Optional<GenerateEntity> optional = generateDao.findOne(example);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @CacheEvict(value = "generateList", allEntries = true)
    @Transactional(readOnly = false)
    public GenerateEntity save(GenerateEntity generateEntity) {
        generateEntity = generateDao.saveAndFlush(generateEntity);
        generate(generateEntity);
        return generateEntity;
    }

    public void generate(GenerateEntity generateEntity) {
        String path = System.getProperty("user.dir") + "/src/main/java/" + generateEntity.getPath().replace(".", "/");
        File ctrlDir = new File(path + "/web");
        if (!ctrlDir.exists()) {
            ctrlDir.mkdirs();
        }
        File serviceDir = new File(path + "/service");
        if (!serviceDir.exists()) {
            serviceDir.mkdirs();
        }
        File daoDir = new File(path + "/dao");
        if (!daoDir.exists()) {
            daoDir.mkdirs();
        }
        File pagesDir = new File(System.getProperty("user.dir") + "/src/main/resources/pages");
        if (!pagesDir.exists()) {
            pagesDir.mkdirs();
        }
        writeFile(path, "web", generateEntity);
        writeFile(path, "service", generateEntity);
        writeFile(path, "dao", generateEntity);
        writePageFile(System.getProperty("user.dir") + "/src/main/resources", "list", generateEntity);
        writePageFile(System.getProperty("user.dir") + "/src/main/resources", "form", generateEntity);

    }

    public void writeFile(String path, String dir, GenerateEntity generateEntity) {

        File ctrl = new File(path + "/" + dir + "/"
                + generateEntity.getEntityName().replace("Entity", StringUtils.toCapitalizeCamelCase(dir.replace("web", "controller"))) + ".java");
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            //模板地址
            String modelPath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                    + "templates/generate/" + StringUtils.toCapitalizeCamelCase(dir) + "Model.model";
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(modelPath)));

            StringBuffer stringBuffer = new StringBuffer();
            reader.lines().forEach(line -> {
                stringBuffer.append(line + "\n");
            });

            String content = stringBuffer.toString();
            content = content.replace("PATH", generateEntity.getPath())
                    .replace("ENTITY_NAME", generateEntity.getEntityName().replace("Entity", ""))
                    .replace("ENTITY_ALIAS", StringUtils.toCamelCase(StringUtils.toUnderScoreCase(generateEntity.getEntityName().replace("Entity", ""))))
                    .replace("DESC", generateEntity.getDescription())
                    .replace("URL", generateEntity.getUrl())
                    .replace("PERMISSION_PREFIX", generateEntity.getPermissionPrefix());

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ctrl), "UTF-8"));
            writer.write(content);
            writer.flush();
            writer.close();
            reader.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writePageFile(String path, String name, GenerateEntity generateEntity) {

        File file = new File(path + "/pages/"
                + generateEntity.getEntityName().replace("Entity", name.replace("list", "Manage").replace("form", "Form") + ".vue"));
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            //模板地址
            String modelPath = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                    + "templates/generate/" + name + ".model";
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(modelPath)));

            StringBuffer stringBuffer = new StringBuffer();
            reader.lines().forEach(line -> {
                stringBuffer.append(line + "\n");
            });

            String content = stringBuffer.toString();
            content = content.replace("${NAME}", generateEntity.getEntityName().replace("Entity", ""))
                    .replace("${ALIAS}", StringUtils.toCamelCase(StringUtils.toUnderScoreCase(generateEntity.getEntityName().replace("Entity", ""))))
                    .replace("${CHINESE}", generateEntity.getDescription())
                    .replace("${PERMISSION}", generateEntity.getPermissionPrefix())
                    .replace("${FORM_NAME}", StringUtils.humpToLine(StringUtils.toCamelCase(StringUtils.toUnderScoreCase(generateEntity.getEntityName().replace("Entity", "")))))
                    .replace("${COLUMNS}", "{}");

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            writer.write(content);
            writer.flush();
            writer.close();
            reader.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public GenerateEntity getById(Integer id) {
        return generateDao.getOne(id);
    }

    @CacheEvict(value = "generateList", allEntries = true)
    @Transactional(readOnly = false)
    public void delete(GenerateEntity generateEntity) {
        generateDao.delete(generateEntity);
    }

    @CacheEvict(value = "generateList", allEntries = true)
    @Transactional(readOnly = false)
    public void deleteById(Integer id) {
        GenerateEntity generateEntity = new GenerateEntity();
        generateEntity.setId(id);
        delete(generateEntity);
    }

    public List<GenerateEntity> getEntityList() {
        String path = System.getProperty("user.dir");
        List<File> list = new ArrayList<>();
        List<GenerateEntity> retList = new ArrayList<>();
        File root = new File(path + "/src");
        getEntityFiles(list, root);
        list.forEach(file -> {
            GenerateEntity generateEntity = new GenerateEntity();
            generateEntity.setEntityName(file.getName().replace(".java", ""));
            generateEntity.setPath(file.getPath().substring(file.getPath().indexOf("java\\") + 5, file.getPath().indexOf("\\entity")).replace("\\", "."));
            retList.add(generateEntity);
        });
        return retList;
    }

    /**
     * 递归获取Entity
     *
     * @param list
     * @param root
     */
    public void getEntityFiles(List<File> list, File root) {
        File[] children = root.listFiles();
        if (children != null && children.length > 0) {
            Arrays.stream(children).forEach(file -> {
                if (file.isDirectory() && file.getName().equals("entity")) {
                    list.addAll(Arrays.asList(file.listFiles()));
                } else {
                    getEntityFiles(list, file);
                }
            });
        }
    }
}
