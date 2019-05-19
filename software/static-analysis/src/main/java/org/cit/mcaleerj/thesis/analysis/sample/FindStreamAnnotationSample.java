package org.cit.mcaleerj.thesis.analysis.sample;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;

import java.util.Optional;

public class FindStreamAnnotationSample {

  public static void findStreamAnnotations(final File projectDir) {
    final ProjectExplorer exp = new ProjectExplorer(new AnnotationProcessor());
    exp.process(projectDir);
  }

  private static class AnnotationProcessor implements ProjectExplorer.FileProcessor {

    public void process(final File file) {

      try {
        new VoidVisitorAdapter<Object>() {
          @Override
          public void visit(final MethodDeclaration decl, final Object arg) {
            super.visit(decl, arg);
            Optional<AnnotationExpr> optional = decl.getAnnotationByClass(org.springframework.cloud.stream.annotation.Input.class);
            optional.ifPresent(annotation -> {
               processStreamInputAnnotation(annotation);
            });
            optional = decl.getAnnotationByClass(org.springframework.cloud.stream.annotation.Output.class);
            optional.ifPresent(annotation -> {
              processStreamOutputAnnotation(annotation);
            });
          }

        }.visit(JavaParser.parse(file), null);

      } catch (IOException e) {
        new RuntimeException(e);
      }
    }

    private void processStreamInputAnnotation(final AnnotationExpr exp) {
      //process input annotation
    }

    private void processStreamOutputAnnotation(final AnnotationExpr exp) {
      //process input annotation
    }

  }

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Please enter the project root directory path");
    } else {
      File baseDir = new File(args[0]);
      findStreamAnnotations(baseDir);
    }
  }

}
